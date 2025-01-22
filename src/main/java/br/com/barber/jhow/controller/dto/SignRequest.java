package br.com.barber.jhow.controller.dto;

public record SignRequest(String email, String password, String name, String role) {
}
