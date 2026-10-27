package com.example.fazendalosardo.dto.veiculos;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ManutencaoListagemRow(
        String origem,
        Long id,
        String ativoNome,
        String tipo,
        String descricao,
        LocalDate data,
        BigDecimal valor,
        Integer kmAtual,
        String formaPagamento,
        String fornecedorNome,
        String vinculoNome,
        String statusPagamento,
        LocalDate dataBaixa,
        Integer numeroParcela,
        Integer totalParcelas
) {
}