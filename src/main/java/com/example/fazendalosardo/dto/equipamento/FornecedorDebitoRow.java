package com.example.fazendalosardo.dto.equipamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FornecedorDebitoRow(
        String origem,
        Long id,
        String nomeAtivo,
        String tipo,
        String descricao,
        LocalDate data,
        BigDecimal valor,
        String statusPagamento,
        LocalDate dataBaixa
) {
}