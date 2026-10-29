package com.example.fazendalosardo.dto.tarefa;

import com.example.fazendalosardo.model.SubTarefa;

public record SubTarefaResponse(
        Long id,
        String titulo,
        Boolean concluida
) {
    public static SubTarefaResponse de(SubTarefa s) {
        return new SubTarefaResponse(s.getId(), s.getTitulo(), s.getConcluida());
    }
}