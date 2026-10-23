package com.example.fazendalosardo.dto.animais;

import java.math.BigDecimal;

public record RacaoIngredienteResponse(
        Long ingredienteId,
        String ingredienteNome,
        BigDecimal quantidade,
        BigDecimal valorUnitario,
        BigDecimal subtotal
) {}