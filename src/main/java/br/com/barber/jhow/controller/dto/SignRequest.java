package br.com.barber.jhow.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record SignRequest(@Email @NotBlank String email,
                          @NotBlank @Min(3) String password,
                          @NotBlank String name,
                          @NotBlank String role) {
}
