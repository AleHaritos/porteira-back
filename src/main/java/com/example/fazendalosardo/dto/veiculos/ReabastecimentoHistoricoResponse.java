package com.example.fazendalosardo.dto.veiculos;

import com.example.fazendalosardo.model.ReabastecimentoCombustivel;
import com.example.fazendalosardo.model.enums.TipoCombustivel;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReabastecimentoHistoricoResponse(
        Long id,
        TipoCombustivel tipoCombustivel,
        LocalDate data,
        BigDecimal litros,
        BigDecimal valorPorLitro,
        String observacao
) {
    public static ReabastecimentoHistoricoResponse de(ReabastecimentoCombustivel r) {
        return new ReabastecimentoHistoricoResponse(
                r.getId(), r.getTipoCombustivel(), r.getData(), r.getLitros(), r.getValorPorLitro(), r.getObservacao()
        );
    }
}