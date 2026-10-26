package com.example.fazendalosardo.dto.equipamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FornecedorDebitoResponse(
        Long id,
        String origem,
        String nomeAtivo,
        String tipo,
        String descricao,
        LocalDate data,
        BigDecimal valor,
        String statusPagamento,
        LocalDate dataBaixa
) {
    public static FornecedorDebitoResponse de(FornecedorDebitoRow row) {
        return new FornecedorDebitoResponse(
                row.id(), row.origem(), row.nomeAtivo(), row.tipo(),
                row.descricao(), row.data(), row.valor(), row.statusPagamento(), row.dataBaixa()
        );
    }
}
