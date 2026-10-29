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
        List<SubTarefaResponse> subtarefas,
        Long negocioId,
        String negocioNome,
        Long safraId,
        String safraNome,
        Long projetoId,
        String projetoNome
) {
    public static TarefaResponse de(Tarefa t) {
        return new TarefaResponse(
                t.getId(), t.getTitulo(), t.getDescricao(), t.getStatus(),
                t.getPrioridade(), t.getPrazo(), t.getOrdem(),
                t.getSubtarefas().stream().map(SubTarefaResponse::de).toList(),
                t.getNegocio() != null ? t.getNegocio().getId() : null,
                t.getNegocio() != null ? t.getNegocio().getNome() : null,
                t.getSafra() != null ? t.getSafra().getId() : null,
                t.getSafra() != null ? t.getSafra().getNome() : null,
                t.getProjeto() != null ? t.getProjeto().getId() : null,
                t.getProjeto() != null ? t.getProjeto().getNome() : null
        );
    }
}