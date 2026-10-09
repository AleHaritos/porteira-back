package com.example.fazendalosardo.dto.negocio;

public record NegocioRequest(
        String nome,
        String descricao,
        String observacoes,
        Long fazendaId
) {
}
