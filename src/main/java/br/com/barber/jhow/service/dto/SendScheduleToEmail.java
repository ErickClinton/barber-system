package br.com.barber.jhow.service.dto;

import br.com.barber.jhow.enums.TypeCutEnum;

import java.time.LocalDateTime;

public record SendScheduleToEmail(LocalDateTime date, String email, String barberName, TypeCutEnum typeCut) {
}
