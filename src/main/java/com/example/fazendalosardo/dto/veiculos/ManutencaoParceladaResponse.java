package com.example.fazendalosardo.dto.veiculos;

import com.example.fazendalosardo.model.ManutencaoParcelada;
import com.example.fazendalosardo.model.enums.TipoManutencao;

import java.math.BigDecimal;

public record ManutencaoParceladaResponse(
        Long id,
        Long veiculoId,
        String veiculoNome,
        Long equipamentoId,
        String equipamentoNome,
        TipoManutencao tipo,
        String descricao,
        BigDecimal valorTotal,
        Integer numeroParcelas,
        String fornecedorNome,
        String vinculoNome,
        String observacao
) {
    public static ManutencaoParceladaResponse de(ManutencaoParcelada m) {
        String vinculo = m.getNegocio() != null ? m.getNegocio().getNome()
                : m.getSafra() != null ? m.getSafra().getNome() : null;
        return new ManutencaoParceladaResponse(
                m.getId(),
                m.getVeiculo() != null ? m.getVeiculo().getId() : null,
                m.getVeiculo() != null ? m.getVeiculo().getNome() : null,
                m.getEquipamento() != null ? m.getEquipamento().getId() : null,
                m.getEquipamento() != null ? m.getEquipamento().getNome() : null,
                m.getTipo(), m.getDescricao(), m.getValorTotal(), m.getNumeroParcelas(),
                m.getFornecedor().getNome(), vinculo, m.getObservacao()
        );
    }
}