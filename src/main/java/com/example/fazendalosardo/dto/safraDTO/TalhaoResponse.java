package com.example.fazendalosardo.dto.safraDTO;

import java.math.BigDecimal;

public record TalhaoResponse(
        Long id,
        String nome,
        BigDecimal areaHectares,
        String localizacao,
        String observacao,
        Boolean ativo,
        Long safraId,
        String safraNome
) {
}
