package com.example.fazendalosardo.dto.veiculos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PostoPagamentoRequest(
        @NotNull @Positive BigDecimal valor,
        @NotNull LocalDate data,
        Long negocioId,
        Long safraId,
        String observacao
) {}