package com.example.fazendalosardo.dto.tarefa;

import com.example.fazendalosardo.model.enums.StatusTarefa;
import jakarta.validation.constraints.NotNull;

public record TarefaStatusRequest(
        @NotNull StatusTarefa status,
        @NotNull Integer ordem
) {
}