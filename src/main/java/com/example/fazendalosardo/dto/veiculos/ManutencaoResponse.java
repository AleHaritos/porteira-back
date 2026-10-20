package com.example.fazendalosardo.dto.veiculos;

import com.example.fazendalosardo.model.Manutencao;
import com.example.fazendalosardo.model.enums.FormaPagamentoManutencao;
import com.example.fazendalosardo.model.enums.TipoManutencao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ManutencaoResponse(
        Long id,
        Long veiculoId,
        String veiculoNome,
        TipoManutencao tipo,
        String descricao,
        LocalDate data,
        BigDecimal valor,
        Integer kmAtual,
        String observacao,
        FormaPagamentoManutencao formaPagamento,
        String fornecedorNome,
        String vinculoNome,
        String statusPagamento,
        LocalDate dataBaixa
) {
    public static ManutencaoResponse de(Manutencao m) {
        String vinculo = m.getNegocio() != null ? m.getNegocio().getNome()
                : m.getSafra() != null ? m.getSafra().getNome() : null;
        String fornecedorNome = m.getFornecedor() != null ? m.getFornecedor().getNome() : null;
        return new ManutencaoResponse(m.getId(), m.getVeiculo().getId(), m.getVeiculo().getNome(),
                m.getTipo(), m.getDescricao(), m.getData(), m.getValor(), m.getKmAtual(), m.getObservacao(),
                m.getFormaPagamento(), fornecedorNome, vinculo,
                m.getStatusPagamento().name(), m.getDataBaixa());
    }
}