package com.example.fazendalosardo.dto.financeiro;

import com.example.fazendalosardo.model.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ContaPendenteRequest(
        String descricao,
        List<BigDecimal> valoresParcelas,
        LocalDate primeiraDataVencimento,
        TipoTransacao tipo,
        Long fazendaId,
        Long negocioId,
        Long safraId,
        String observacao
) {
}
