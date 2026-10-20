package com.example.fazendalosardo.dto.safraDTO;

import java.math.BigDecimal;

public record CustosCombustivelManutencaoResponse(
        BigDecimal totalCombustivel,
        BigDecimal totalManutencao
) {
}