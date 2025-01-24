package br.com.barber.jhow.controller.dto;


import java.time.LocalDateTime;
import java.util.UUID;

public record CreateScheduleRequest(LocalDateTime scheduled,
                                    String typeOfCut,
                                    String phone,
                                    String email,
                                    String name,
                                    UUID barbear) {
}
