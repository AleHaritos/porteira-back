package com.example.fazendalosardo.dto.animais;

import java.math.BigDecimal;

public record IngredienteResponse(
        Long id,
        String nome,
        BigDecimal valorUnitario,
        String unidade,
        Boolean ativo
) {}