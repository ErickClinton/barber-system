package br.com.barber.jhow.cron;

import br.com.barber.jhow.cron.dto.SchedullesTodayDto;
import br.com.barber.jhow.service.AwsSnsService;
import br.com.barber.jhow.service.SchedulingService;

import br.com.barber.jhow.service.dto.MessageDto;
import com.amazonaws.services.sns.model.Topic;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;


@Component
public class SchedulesCron {

    private final SchedulingService schedulingService;
    private final AwsSnsService awsSnsService;
    private final ObjectMapper objectMapper;
    private final Topic sendEmailTopic;

    public SchedulesCron(SchedulingService schedulingService,
                         AwsSnsService awsSnsService,
                         ObjectMapper objectMapper, @Qualifier("emitEmailEventsTopic") Topic sendEmailTopic) {
        this.schedulingService = schedulingService;
        this.awsSnsService = awsSnsService;
        this.objectMapper = objectMapper;
        this.sendEmailTopic = sendEmailTopic;
    }

    @Scheduled(cron = "${cron.daily.morning}")
    public void daysSchedules() throws JsonProcessingException {
        var schedules = this.schedulingService.getSchedulingByDate(LocalDateTime.now());

        for (var scheduling : schedules) {
            var dataToClientForSchedule = new SchedullesTodayDto(scheduling.getEmail(),
                    scheduling.getScheduled(),
                    scheduling.getBarber().getName(),scheduling.getTypeOfCut());
            
            String jsonMessage = this.objectMapper.writeValueAsString(dataToClientForSchedule);
            this.awsSnsService.publish(this.sendEmailTopic.getTopicArn(),new MessageDto(jsonMessage));
        }
    }
}


