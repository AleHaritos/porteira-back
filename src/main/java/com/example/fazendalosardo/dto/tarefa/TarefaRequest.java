package com.example.fazendalosardo.dto.tarefa;

import com.example.fazendalosardo.model.enums.PrioridadeTarefa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record TarefaRequest(
        @NotNull Long fazendaId,
        @NotBlank String titulo,
        String descricao,
        @NotNull PrioridadeTarefa prioridade,
        LocalDate prazo,
        List<String> subtarefas,
        Long negocioId,
        Long safraId,
        Long projetoId
) {
}