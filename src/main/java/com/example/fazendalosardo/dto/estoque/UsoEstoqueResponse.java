package com.example.fazendalosardo.dto.estoque;

import com.example.fazendalosardo.model.estoque.UsoEstoque;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UsoEstoqueResponse(
        Long id,
        BigDecimal quantidade,
        LocalDate data,
        String observacao
) {
    public static UsoEstoqueResponse de(UsoEstoque u) {
        return new UsoEstoqueResponse(u.getId(), u.getQuantidade(), u.getData(), u.getObservacao());
    }
}