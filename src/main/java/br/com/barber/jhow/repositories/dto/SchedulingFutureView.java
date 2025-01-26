package br.com.barber.jhow.repositories.dto;

import br.com.barber.jhow.enums.TypeCutEnum;

import java.time.LocalDateTime;

public interface SchedulingFutureView {
    LocalDateTime getScheduled();
    TypeCutEnum getTypeOfCut();
    String getName();
    String getPhone();
    String getEmail();
}
