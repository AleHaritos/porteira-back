package com.example.fazendalosardo.dto.animais;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PiqueteRequest(
        @NotBlank String nome,
        @NotNull @Positive BigDecimal areaHectares,
        @NotNull Long fazendaId
) {
}