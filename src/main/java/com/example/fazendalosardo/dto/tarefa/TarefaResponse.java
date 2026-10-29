package com.example.fazendalosardo.dto.tarefa;

import com.example.fazendalosardo.model.Tarefa;
import com.example.fazendalosardo.model.enums.PrioridadeTarefa;
import com.example.fazendalosardo.model.enums.StatusTarefa;

import java.time.LocalDate;
import java.util.List;

public record TarefaResponse(
        Long id,
        String titulo,
        String descricao,
        StatusTarefa status,
        PrioridadeTarefa prioridade,
        LocalDate prazo,
        Integer ordem,
        List<SubTarefaResponse> subtarefas
) {
    public static TarefaResponse de(Tarefa t) {
        return new TarefaResponse(t.getId(), t.getTitulo(), t.getDescricao(), t.getStatus(),
                t.getPrioridade(), t.getPrazo(), t.getOrdem(),
                t.getSubtarefas().stream().map(SubTarefaResponse::de).toList());
    }
}