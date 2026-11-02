package com.example.fazendalosardo.dto;

import com.example.fazendalosardo.model.Nota;

import java.time.Instant;

public record NotaResponse(
        Long id,
        Long fazendaId,
        String titulo,
        String corpo,
        Long negocioId,
        String negocioNome,
        Long safraId,
        String safraNome,
        Long projetoId,
        String projetoNome,
        Instant criadoEm,
        Instant atualizadoEm
) {
    public static NotaResponse de(Nota n) {
        return new NotaResponse(
                n.getId(),
                n.getFazenda().getId(),
                n.getTitulo(),
                n.getCorpo(),
                n.getNegocio() != null ? n.getNegocio().getId() : null,
                n.getNegocio() != null ? n.getNegocio().getNome() : null,
                n.getSafra() != null ? n.getSafra().getId() : null,
                n.getSafra() != null ? n.getSafra().getNome() : null,
                n.getProjeto() != null ? n.getProjeto().getId() : null,
                n.getProjeto() != null ? n.getProjeto().getNome() : null,
                n.getCriadoEm(),
                n.getAtualizadoEm()
        );
    }
}