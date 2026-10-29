package com.example.fazendalosardo.dto.tarefa;

import jakarta.validation.constraints.NotBlank;

public record SubTarefaRequest(
        @NotBlank String titulo
) {
}