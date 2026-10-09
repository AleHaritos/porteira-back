package com.example.fazendalosardo.dto.negocio;

public record NegocioResponse(
        Long id,
        String nome,
        String descricao,
        Boolean ativo,
        String observacoes,
        Long fazendaId,
        String fazendaNome
) {
}
