package com.example.fazendalosardo.dto.financeiro;

import com.example.fazendalosardo.model.enums.TipoTransacao;

import java.math.BigDecimal;

public record ContaPendenteResponse(
        Long id,
        String descricao,
        BigDecimal valorTotal,
        Integer numeroParcelas,
        TipoTransacao tipo,
        Long fazendaId,
        Long negocioId,
        String negocioNome,
        Long safraId,
        String safraNome,
        String observacao
) {
}
