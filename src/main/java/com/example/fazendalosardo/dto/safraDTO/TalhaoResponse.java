package com.example.fazendalosardo.dto.safraDTO;

import com.example.fazendalosardo.model.enums.TipoManejo;

import java.math.BigDecimal;

public record TalhaoResponse(
        Long id,
        String nome,
        BigDecimal areaHectares,
        String localizacao,
        String observacao,
        TipoManejo statusTalhao,
        Boolean ativo,
        Long safraId,
        String safraNome
) {
}