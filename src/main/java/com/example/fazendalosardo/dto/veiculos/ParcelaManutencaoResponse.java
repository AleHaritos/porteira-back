package com.example.fazendalosardo.dto.veiculos;

import com.example.fazendalosardo.model.ParcelaManutencao;
import com.example.fazendalosardo.model.ManutencaoParcelada;
import com.example.fazendalosardo.model.enums.TipoManutencao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ParcelaManutencaoResponse(
        Long id,
        Integer numeroParcela,
        Integer totalParcelas,
        BigDecimal valor,
        LocalDate dataVencimento,
        String status,
        LocalDate dataBaixa,
        Long manutencaoParceladaId,
        String descricao,
        String ativoNome,
        TipoManutencao tipo,
        String fornecedorNome,
        String vinculoNome
) {
    public static ParcelaManutencaoResponse de(ParcelaManutencao p) {
        ManutencaoParcelada m = p.getManutencaoParcelada();
        String vinculo = m.getNegocio() != null ? m.getNegocio().getNome()
                : m.getSafra() != null ? m.getSafra().getNome()
                : m.getProjeto() != null ? m.getProjeto().getNome() : null;
        String ativoNome = m.getVeiculo() != null ? m.getVeiculo().getNome() : m.getEquipamento().getNome();
        return new ParcelaManutencaoResponse(
                p.getId(), p.getNumeroParcela(), m.getNumeroParcelas(), p.getValor(), p.getDataVencimento(),
                p.getStatus().name(), p.getDataBaixa(), m.getId(), m.getDescricao(), ativoNome, m.getTipo(),
                m.getFornecedor().getNome(), vinculo
        );
    }
}
