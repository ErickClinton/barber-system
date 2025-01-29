package br.com.barber.jhow.service;

import br.com.barber.jhow.controller.dto.*;
import br.com.barber.jhow.entities.SchedulingEntity;
import br.com.barber.jhow.entities.UserEntity;
import br.com.barber.jhow.enums.TypeCutEnum;
import br.com.barber.jhow.exceptions.scheduling.ScheduleCancellationException;
import br.com.barber.jhow.exceptions.scheduling.SchedulingHourConflictException;
import br.com.barber.jhow.exceptions.scheduling.SchedulingNotExistException;
import br.com.barber.jhow.repositories.SchedulingRepository;
import br.com.barber.jhow.repositories.dto.SchedulingView;
import br.com.barber.jhow.service.dto.MessageDto;
import br.com.barber.jhow.service.dto.SendScheduleToEmail;
import com.amazonaws.services.sns.model.Topic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.io.UncheckedIOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class SchedulingService {

    private final SchedulingRepository schedulingRepository;
    private final UserService userService;
    private final AwsSnsService awsSnsService;
    private final Topic sendEmailTopic;


    public SchedulingService(SchedulingRepository schedulingRepository,
                             @Lazy UserService userService, AwsSnsService awsSnsService, @Qualifier("emitEmailEventsTopic") Topic sendEmailTopic) {
        this.schedulingRepository = schedulingRepository;
        this.userService = userService;
        this.awsSnsService = awsSnsService;
        this.sendEmailTopic = sendEmailTopic;
    }

    public SchedulingEntity createSchedule(CreateScheduleRequest createScheduleRequest, Jwt jwtToken) {

        if (!this.verifyHour(createScheduleRequest.barbear(),createScheduleRequest.scheduled()))
                throw new SchedulingHourConflictException("Hour regristred");

        var typeCut = TypeCutEnum.fromTypeCut(createScheduleRequest.typeOfCut());
        var endService = createScheduleRequest.scheduled().plusMinutes(typeCut.getTime());
        var barber = this.userService.getUserById(createScheduleRequest.barbear());
        boolean isJwtTokenValid = jwtToken != null && jwtToken.getClaimAsString("sub") != null;

        var schedule = isJwtTokenValid
                ? this.createScheduleWithRegister(createScheduleRequest, endService, typeCut, jwtToken,barber)
                : this.createScheduleNoRegister(createScheduleRequest, endService, typeCut, barber);


        this.sendEmailTopic(schedule);
        return this.schedulingRepository.save(schedule);
    }

    public ScheduleAppointmentResponse scheduleByBarberId(UUID id, LocalDateTime period, Integer page, Integer pageSize) {

        var pageRequest = PageRequest.of(page, pageSize);
        var schedules = this.schedulingRepository.findByBarberIdAndScheduledBetween(id,LocalDateTime.now(),period,pageRequest);

        return new ScheduleAppointmentResponse(schedules.getContent(),new PaginationDto(page,pageSize,schedules.getTotalElements(),schedules.getTotalPages()));
    }

    public void deleteSchedule(Integer id) {
        var scheduleEntity = this.schedulingRepository.findById(id)
                .orElseThrow(() -> new SchedulingNotExistException("Schedule not exist"));

        boolean deadlineToCancel = LocalDateTime.now().plusHours(2)
                .isAfter(scheduleEntity.getScheduled());
        if (deadlineToCancel) {
            throw new ScheduleCancellationException("It is not possible to cancel as it is too close to the scheduled time or has already passed.");
        }

        this.schedulingRepository.deleteById(id);
    }

    public List<SchedulingEntity> getSchedulingByDate(LocalDateTime date) {
        return this.schedulingRepository.findByScheduled(date);
    }

    public boolean verifyHour(UUID barberId, LocalDateTime scheduled) {
        var hoursSchedule = this.schedulingRepository.findByBarberId(barberId);

        return hoursSchedule.stream()
                .noneMatch(scheduling -> isTimeConflict(scheduling, scheduled));
    }


    public Page<SchedulingDto> appointmentHistoryByUserID(UUID userId, Integer page, Integer pageSize) {
        var pageRequest = PageRequest.of(page, pageSize);
        return  this.schedulingRepository.findByUserId(userId,pageRequest)
                .map(this::mapToDto);
    }

    private boolean isTimeConflict(SchedulingEntity scheduling, LocalDateTime scheduled) {
        return !scheduled.isBefore(scheduling.getScheduled()) && !scheduled.isAfter(scheduling.getEndService());
    }

    private SchedulingDto mapToDto(SchedulingView schedulingView) {
        return new SchedulingDto(
                schedulingView.getScheduled(),
                schedulingView.getEndService(),
                schedulingView.getTypeOfCut(),
                schedulingView.getBarber().getName()
        );
    }

    private SchedulingEntity createScheduleWithRegister(CreateScheduleRequest createScheduleRequest, LocalDateTime endService, TypeCutEnum typeCut, Jwt jwtToken, UserEntity barbear) {
        var token = jwtToken.getClaimAsString("sub");
        var user = this.userService.getUserById(UUID.fromString(token));
        return new SchedulingEntity(
                createScheduleRequest.scheduled(),
                endService,
                typeCut,
                user.getEmail(),
                createScheduleRequest.phone(),
                user.getName(),
                user,
                barbear
        );
    }

    private SchedulingEntity createScheduleNoRegister(CreateScheduleRequest createScheduleRequest, LocalDateTime endService, TypeCutEnum typeCut, UserEntity barbear) {
        return new SchedulingEntity(
                createScheduleRequest.scheduled(),
                endService,
                typeCut,
                createScheduleRequest.email(),
                createScheduleRequest.phone(),
                createScheduleRequest.name(),
                barbear
        );
    }

    private void sendEmailTopic(SchedulingEntity scheduling) {
        var scheduleToEmail = new SendScheduleToEmail(scheduling.getScheduled(),scheduling.getEmail(),scheduling.getBarber().getName(),scheduling.getTypeOfCut());
        var objectMapper = new ObjectMapper();
        String scheduleToEmailString = null;
        try {
            scheduleToEmailString = objectMapper.writeValueAsString(scheduleToEmail);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
        this.awsSnsService.publish(this.sendEmailTopic.getTopicArn(),new MessageDto(scheduleToEmailString));
    }

}

