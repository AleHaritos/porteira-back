package com.example.fazendalosardo.dto.estoque;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CompraEstoqueRequest(
        @NotNull @Positive BigDecimal quantidade,
        @NotNull @Positive BigDecimal valorUnitario,
        @NotNull LocalDate data,
        String observacao
) {
}