package com.example.fazendalosardo.dto;

public record TokenResponse(
        String token,
        Long id,
        String nome,
        String numero,
        Boolean admin
) {}
