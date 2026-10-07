package com.example.fazendalosardo.dto.safraDTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProdutoSafraRequest(
        @NotBlank String nome,
        @NotNull @Positive BigDecimal custo,
        @NotNull Long safraId
) {
}
