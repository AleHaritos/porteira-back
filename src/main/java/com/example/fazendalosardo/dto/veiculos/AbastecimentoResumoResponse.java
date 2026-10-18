package com.example.fazendalosardo.dto.veiculos;

import java.math.BigDecimal;

public record AbastecimentoResumoResponse(
        BigDecimal totalValor,
        BigDecimal totalLitros,
        Long quantidade
) {
}