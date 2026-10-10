package com.example.fazendalosardo.dto.financeiro;

import com.example.fazendalosardo.model.enums.TipoTransacao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParcelaContaPendenteResponse(
        Long id,
        Integer numeroParcela,
        Integer totalParcelas,
        BigDecimal valor,
        LocalDate dataVencimento,
        String status,
        LocalDate dataBaixa,
        TipoTransacao tipo,
        Long contaPendenteId,
        String descricao,
        String negocioNome,
        String safraNome
) {
}
