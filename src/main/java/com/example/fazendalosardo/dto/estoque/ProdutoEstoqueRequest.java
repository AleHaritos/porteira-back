package com.example.fazendalosardo.dto.estoque;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoEstoqueRequest(
        @NotNull Long fazendaId,
        @NotBlank String nome
) {
}