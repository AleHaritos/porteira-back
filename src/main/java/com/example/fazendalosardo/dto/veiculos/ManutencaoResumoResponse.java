package com.example.fazendalosardo.dto.veiculos;

import java.math.BigDecimal;

public record ManutencaoResumoResponse(BigDecimal totalValor, long quantidade) {
}