package com.example.fazendalosardo.dto.animais;

import java.math.BigDecimal;

public record PiqueteResponse(
        Long id,
        String nome,
        BigDecimal areaHectares,
        Boolean ativo,
        long quantidadeAnimais
) {
}