package br.com.barber.jhow.controller.dto;

public record LoginResponse(String accessToken, Long expiresIn) {
}
