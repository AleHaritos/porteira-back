package com.example.fazendalosardo.dto.negocio;

public record NegocioUpdateRequest(
        String nome,
        String descricao,
        String observacoes
) {
}
