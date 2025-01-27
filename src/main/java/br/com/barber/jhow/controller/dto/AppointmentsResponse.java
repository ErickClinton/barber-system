package br.com.barber.jhow.controller.dto;

import java.util.List;

public record AppointmentsResponse(List<SchedulingDto> scheduling , PaginationDto paginationDto) {
}
