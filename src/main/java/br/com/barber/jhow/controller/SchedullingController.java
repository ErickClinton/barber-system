package br.com.barber.jhow.controller;

import br.com.barber.jhow.controller.dto.CreateScheduleRequest;
import br.com.barber.jhow.service.SchedulingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/scheduling")
public class SchedullingController {

    private final SchedulingService schedulingService;

    public SchedullingController(SchedulingService schedulingService) {
        this.schedulingService = schedulingService;
    }

    @PostMapping
    public ResponseEntity<Void> createSchedule(@AuthenticationPrincipal Jwt jwtToken,
                                               @RequestBody CreateScheduleRequest request) {

        var schedule = this.schedulingService.createSchedule(request,jwtToken);
        return ResponseEntity.created(URI.create("/schedule/"+ schedule.getId().toString())).build();
    }

}
