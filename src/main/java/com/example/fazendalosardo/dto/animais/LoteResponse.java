package com.example.fazendalosardo.dto.animais;

public record LoteResponse(
        Long id,
        String nome,
        Integer quantidade,
        String observacoes,
        Boolean ativo
) {
}
