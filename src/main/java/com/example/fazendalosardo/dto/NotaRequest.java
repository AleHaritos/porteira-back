package com.example.fazendalosardo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NotaRequest(
        @NotNull Long fazendaId,
        @NotBlank @Size(max = 150) String titulo,
        String corpo,
        Long negocioId,
        Long safraId,
        Long projetoId
) {
}