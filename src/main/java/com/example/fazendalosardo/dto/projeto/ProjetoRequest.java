package com.example.fazendalosardo.dto.projeto;

public record ProjetoRequest(
        String nome,
        String descricao,
        String observacoes,
        Long fazendaId
) {
}