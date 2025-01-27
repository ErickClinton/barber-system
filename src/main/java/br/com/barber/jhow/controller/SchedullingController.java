package br.com.barber.jhow.controller;

import br.com.barber.jhow.controller.dto.*;
import br.com.barber.jhow.service.SchedulingService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
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
                                               @RequestBody CreateScheduleRequest createScheduleRequest) {

        var schedule = this.schedulingService.createSchedule(createScheduleRequest,jwtToken);
        return ResponseEntity.created(URI.create("/schedule/"+ schedule.getId().toString())).build();
    }

    @GetMapping("history")
    public ResponseEntity<AppointmentsResponse> scheduleHistoryByUserId(@AuthenticationPrincipal Jwt jwtToken,
                                                         @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                         @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        var id = jwtToken.getClaimAsString("sub");
        var schedule = this.schedulingService.appointmentHistoryByUserID(UUID.fromString(id),page,pageSize);
        return ResponseEntity.ok(new AppointmentsResponse(schedule.getContent(),
                new PaginationDto(page,pageSize,schedule.getTotalElements(),schedule.getTotalPages())));
    }

    @PreAuthorize("hasAuthority('ROLE_BARBER')")
    @GetMapping("period/{date}")
    public ResponseEntity<ScheduleAppointmentResponse> scheduleByBarberId(@AuthenticationPrincipal Jwt jwtToken, @PathVariable("date") String date,
                                                                        @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        var schedule = this.schedulingService.scheduleByBarberId(jwtToken, LocalDateTime.parse(date),page,pageSize);
        return ResponseEntity.ok(schedule);
    }

}
