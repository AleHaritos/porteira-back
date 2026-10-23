package com.example.fazendalosardo.dto.animais;

import java.math.BigDecimal;

public record AnimalCustoResponse(
        BigDecimal custoVacinacao,
        BigDecimal custoAlimentacao,
        BigDecimal valorCompra,
        BigDecimal custoTotal
) {}