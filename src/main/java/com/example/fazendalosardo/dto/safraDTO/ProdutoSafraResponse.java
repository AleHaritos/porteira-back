package com.example.fazendalosardo.dto.safraDTO;

import java.math.BigDecimal;

public record ProdutoSafraResponse(
        Long id,
        String nome,
        BigDecimal custo,
        Long safraId,
        String safraNome
) {
}
