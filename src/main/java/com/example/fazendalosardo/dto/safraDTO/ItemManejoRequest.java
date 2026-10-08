package com.example.fazendalosardo.dto.safraDTO;

import java.math.BigDecimal;

public record ItemManejoRequest(
        Long produtoSafraId,
        BigDecimal quantidade
) {
}
