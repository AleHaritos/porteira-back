package com.example.fazendalosardo.dto.projeto;

import com.example.fazendalosardo.model.enums.StatusProjeto;

public record ProjetoUpdateRequest(
        String nome,
        String descricao,
        String observacoes,
        StatusProjeto status
) {
}