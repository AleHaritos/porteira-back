package com.example.fazendalosardo.dto.projeto;

import com.example.fazendalosardo.model.enums.StatusProjeto;

public record ProjetoResponse(
        Long id,
        String nome,
        String descricao,
        Boolean ativo,
        StatusProjeto status,
        String observacoes,
        Long fazendaId,
        String fazendaNome
) {
}