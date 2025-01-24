package br.com.barber.jhow.repositories.dto;

import br.com.barber.jhow.entities.UserEntity;
import br.com.barber.jhow.enums.TypeCutEnum;

import java.time.LocalDateTime;

public interface SchedulingView {
     LocalDateTime getScheduled();
     LocalDateTime getEndService();
     TypeCutEnum getTypeOfCut();
     UserEntity getBarber();
}
