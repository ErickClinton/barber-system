package br.com.barber.jhow.service;

import br.com.barber.jhow.controller.dto.CreateScheduleRequest;
import br.com.barber.jhow.entities.SchedulingEntity;
import br.com.barber.jhow.entities.UserEntity;
import br.com.barber.jhow.enums.TypeCutEnum;
import br.com.barber.jhow.exceptions.scheduling.SchedulingHourConflictException;
import br.com.barber.jhow.repositories.SchedulingRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SchedulingService {

    private final SchedulingRepository schedulingRepository;
    private final UserService userService;

    public SchedulingService(SchedulingRepository schedulingRepository, UserService userService) {
        this.schedulingRepository = schedulingRepository;
        this.userService = userService;
    }

    public SchedulingEntity createSchedule(CreateScheduleRequest createScheduleRequest, Jwt jwtToken) {

        if (!this.verifyHour(createScheduleRequest.barbear(),createScheduleRequest.scheduled()) )
                throw new SchedulingHourConflictException("Hour regristred");

        var typeCut = TypeCutEnum.fromTypeCut(createScheduleRequest.typeOfCut());
        var endService = createScheduleRequest.scheduled().plusMinutes(typeCut.getTime());
        var barbear = this.userService.getById(createScheduleRequest.barbear());
        boolean isJwtTokenValid = jwtToken != null && jwtToken.getClaimAsString("sub") != null;

        var schedule = isJwtTokenValid
                ? this.createScheduleWithRegister(createScheduleRequest, endService, typeCut, jwtToken,barbear)
                : this.createScheduleNoRegister(createScheduleRequest, endService, typeCut, barbear);

        return this.schedulingRepository.save(schedule);
    }

    private SchedulingEntity createScheduleWithRegister(CreateScheduleRequest createScheduleRequest, LocalDateTime endService, TypeCutEnum typeCut, Jwt jwtToken, UserEntity barbear) {
        var token = jwtToken.getClaimAsString("sub");
        var user = this.userService.getById(UUID.fromString(token));
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

    public boolean verifyHour(UUID barberId, LocalDateTime scheduled) {
        var hoursSchedule = this.schedulingRepository.findByBarberId(barberId);

        return hoursSchedule.stream()
                .noneMatch(scheduling -> isTimeConflict(scheduling, scheduled));
    }

    private boolean isTimeConflict(SchedulingEntity scheduling, LocalDateTime scheduled) {
        return !scheduled.isBefore(scheduling.getScheduled()) && !scheduled.isAfter(scheduling.getEndService());
    }

}

