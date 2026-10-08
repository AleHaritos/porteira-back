package com.example.fazendalosardo.dto.safraDTO;

import java.math.BigDecimal;

public record ItemManejoResponse(
        Long id,
        Long produtoSafraId,
        String produtoNome,
        BigDecimal quantidade,
        BigDecimal custoUnitario,
        BigDecimal custoTotal
) {
}
