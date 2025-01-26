package br.com.barber.jhow.controller.dto;

import br.com.barber.jhow.repositories.dto.SchedulingFutureView;

import java.util.List;

public record ScheduleAppointmentResponse(List<SchedulingFutureView> schedules,PaginationDto paginationDto) {
}
