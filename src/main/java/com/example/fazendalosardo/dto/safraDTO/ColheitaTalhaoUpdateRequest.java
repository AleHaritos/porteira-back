package com.example.fazendalosardo.dto.safraDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ColheitaTalhaoUpdateRequest(
        String descricao,
        LocalDate data,
        @NotNull @PositiveOrZero BigDecimal quantidadeColhida,
        @NotNull @PositiveOrZero BigDecimal valorUnitario
) {
}