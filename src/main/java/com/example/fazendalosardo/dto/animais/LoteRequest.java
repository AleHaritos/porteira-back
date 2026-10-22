package com.example.fazendalosardo.dto.animais;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoteRequest(
        @NotBlank String nome,
        String observacoes,
        @NotNull Long fazendaId
) {
}