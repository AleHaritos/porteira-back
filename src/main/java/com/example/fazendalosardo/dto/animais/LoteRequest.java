package com.example.fazendalosardo.dto.animais;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record LoteRequest(
        @NotBlank String nome,
        @NotNull @Positive Integer quantidade,
        String observacoes,
        @NotNull Long fazendaId
) {
}