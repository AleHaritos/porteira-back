package com.example.fazendalosardo.dto.animais;

public record LoteResponse(
        Long id,
        String nome,
        String observacoes,
        Boolean ativo,
        long quantidadeAnimais
) {
}