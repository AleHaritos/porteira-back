package com.example.fazendalosardo.dto.animais;

import java.math.BigDecimal;

public record OperacaoVendaItemResponse(
        Long animalId,
        String animalTipo,
        String animalDescricao,
        String animalBrinco,
        BigDecimal custoVacinacao,
        BigDecimal custoAlimentacao,
        BigDecimal valorCompra,
        BigDecimal custoTotal
) {}