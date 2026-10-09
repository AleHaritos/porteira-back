package com.example.fazendalosardo.dto.financeiro;

import com.example.fazendalosardo.model.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoResponse(
        Long id,
        LocalDate data,
        BigDecimal valor,
        TipoTransacao tipo,
        String descricao,
        String observacao,
        Boolean ativo,
        Long fazendaId,
        String fazendaNome,
        Long negocioId,
        String negocioNome,
        Long safraId,
        String safraNome
) {
}
