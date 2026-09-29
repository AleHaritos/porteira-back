package com.example.fazendalosardo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record FazendaRequest(
        @NotBlank
        String nome,
        @NotNull
        @Positive
        BigDecimal hectares,
        String localizacao
) {
}
