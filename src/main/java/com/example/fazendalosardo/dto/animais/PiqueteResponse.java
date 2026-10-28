package com.example.fazendalosardo.dto.animais;

import com.example.fazendalosardo.model.enums.StatusPiquete;

import java.math.BigDecimal;

public record PiqueteResponse(
        Long id,
        String nome,
        BigDecimal areaHectares,
        StatusPiquete status,
        Boolean ativo,
        long quantidadeAnimais
) {
}