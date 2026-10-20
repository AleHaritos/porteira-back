package com.example.fazendalosardo.dto.safraDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

public record ColheitaTalhaoRequest(
        @NotNull Long talhaoId,
        String descricao,
        @NotNull @PositiveOrZero BigDecimal quantidadeColhida,
        @NotNull @PositiveOrZero BigDecimal valorUnitario
) {
}