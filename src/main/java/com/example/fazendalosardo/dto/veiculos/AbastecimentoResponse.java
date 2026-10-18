package com.example.fazendalosardo.dto.veiculos;

import com.example.fazendalosardo.model.Abastecimento;
import com.example.fazendalosardo.model.enums.LocalAbastecimento;
import com.example.fazendalosardo.model.enums.TipoCombustivel;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AbastecimentoResponse(
        Long id,
        Long veiculoId,
        String veiculoNome,
        LocalDate data,
        BigDecimal litros,
        BigDecimal valorPorLitro,
        BigDecimal valorTotal,
        TipoCombustivel tipoCombustivel,
        LocalAbastecimento localAbastecimento,
        Long postoId,
        String postoNome,
        String observacao,
        String vinculoNome,
        String statusPagamento,
        LocalDate dataBaixa
) {
    public static AbastecimentoResponse de(Abastecimento a) {
        String vinculo = a.getNegocio() != null ? a.getNegocio().getNome()
                : a.getSafra() != null ? a.getSafra().getNome() : null;
        return new AbastecimentoResponse(a.getId(), a.getVeiculo().getId(), a.getVeiculo().getNome(),
                a.getData(), a.getLitros(), a.getValorPorLitro(), a.getValorTotal(),
                a.getTipoCombustivel(), a.getLocalAbastecimento(),
                a.getPosto() != null ? a.getPosto().getId() : null,
                a.getPosto() != null ? a.getPosto().getNome() : null,
                a.getObservacao(), vinculo,
                a.getStatusPagamento() != null ? a.getStatusPagamento().name() : null,
                a.getDataBaixa());
    }
}