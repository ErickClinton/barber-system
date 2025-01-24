package br.com.barber.jhow.controller.dto;

import br.com.barber.jhow.enums.TypeCutEnum;

import java.time.LocalDateTime;

public record SchedulingDto(LocalDateTime scheduled,
                            LocalDateTime endService,
                            TypeCutEnum typeCutEnum,
                            String barbearName) {
}
