package com.example.fazendalosardo.dto.estoque;

import com.example.fazendalosardo.model.estoque.ProdutoEstoque;

import java.math.BigDecimal;

public record ProdutoEstoqueResponse(
        Long id,
        String nome,
        BigDecimal valor,
        BigDecimal quantidade
) {
    public static ProdutoEstoqueResponse de(ProdutoEstoque p) {
        return new ProdutoEstoqueResponse(p.getId(), p.getNome(), p.getValor(), p.getQuantidade());
    }
}
