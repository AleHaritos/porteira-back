package com.example.fazendalosardo.dto.equipamento;

import com.example.fazendalosardo.dto.veiculos.ManutencaoListagemRow;
import com.example.fazendalosardo.model.ManutencaoEquipamento;
import com.example.fazendalosardo.model.enums.FormaPagamentoManutencao;
import com.example.fazendalosardo.model.enums.TipoManutencao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ManutencaoEquipamentoResponse(
        Long id,
        Long equipamentoId,
        String equipamentoNome,
        TipoManutencao tipo,
        String descricao,
        LocalDate data,
        BigDecimal valor,
        String observacao,
        FormaPagamentoManutencao formaPagamento,
        String fornecedorNome,
        String vinculoNome,
        String statusPagamento,
        LocalDate dataBaixa,
        String origem,
        Integer numeroParcela,
        Integer totalParcelas
) {
    public static ManutencaoEquipamentoResponse de(ManutencaoEquipamento m) {
        String vinculo = m.getNegocio() != null ? m.getNegocio().getNome()
                : m.getSafra() != null ? m.getSafra().getNome() : null;
        String fornecedorNome = m.getFornecedor() != null ? m.getFornecedor().getNome() : null;
        return new ManutencaoEquipamentoResponse(m.getId(), m.getEquipamento().getId(), m.getEquipamento().getNome(),
                m.getTipo(), m.getDescricao(), m.getData(), m.getValor(), m.getObservacao(),
                m.getFormaPagamento(), fornecedorNome, vinculo,
                m.getStatusPagamento().name(), m.getDataBaixa(), "SIMPLES", null, null);
    }

    public static ManutencaoEquipamentoResponse deListagem(ManutencaoListagemRow row) {
        return new ManutencaoEquipamentoResponse(
                row.id(), null, row.ativoNome(), TipoManutencao.valueOf(row.tipo()), row.descricao(),
                row.data(), row.valor(), null,
                FormaPagamentoManutencao.valueOf(row.formaPagamento()), row.fornecedorNome(), row.vinculoNome(),
                row.statusPagamento(), row.dataBaixa(), row.origem(), row.numeroParcela(), row.totalParcelas()
        );
    }
}