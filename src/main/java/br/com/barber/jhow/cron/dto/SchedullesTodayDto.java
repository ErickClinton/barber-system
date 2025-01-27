package br.com.barber.jhow.cron.dto;

import br.com.barber.jhow.enums.TypeCutEnum;

import java.time.LocalDateTime;

public record SchedullesTodayDto(String email, LocalDateTime date, String barberName, TypeCutEnum typeCut) {
}
