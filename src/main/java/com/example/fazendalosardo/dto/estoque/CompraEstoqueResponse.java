package com.example.fazendalosardo.dto.estoque;

import com.example.fazendalosardo.model.estoque.CompraEstoque;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CompraEstoqueResponse(
        Long id,
        BigDecimal quantidade,
        BigDecimal valorUnitario,
        LocalDate data,
        String observacao
) {
    public static CompraEstoqueResponse de(CompraEstoque c) {
        return new CompraEstoqueResponse(c.getId(), c.getQuantidade(), c.getValorUnitario(), c.getData(), c.getObservacao());
    }
}