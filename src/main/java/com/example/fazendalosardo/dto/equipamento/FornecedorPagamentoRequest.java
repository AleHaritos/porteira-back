package com.example.fazendalosardo.dto.equipamento;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FornecedorPagamentoRequest(
        @NotNull @Positive BigDecimal valor,
        @NotNull LocalDate data,
        Long negocioId,
        Long safraId
) {
}
