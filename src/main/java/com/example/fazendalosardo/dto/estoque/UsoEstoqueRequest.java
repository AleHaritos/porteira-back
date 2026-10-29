package com.example.fazendalosardo.dto.estoque;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UsoEstoqueRequest(
        @NotNull @Positive BigDecimal quantidade,
        @NotNull LocalDate data,
        String observacao
) {
}