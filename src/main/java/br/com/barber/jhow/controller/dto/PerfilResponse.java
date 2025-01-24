package br.com.barber.jhow.controller.dto;

import java.util.List;

public record PerfilResponse(String name,
                             String email,
                             List<SchedulingDto> schedulingDto,
                             PaginationDto pagination) {
}
