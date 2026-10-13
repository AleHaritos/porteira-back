package com.example.fazendalosardo.dto.veiculos;

import com.example.fazendalosardo.model.EstoqueCombustivel;
import com.example.fazendalosardo.model.enums.TipoCombustivel;

import java.math.BigDecimal;

public record EstoqueCombustivelResponse(
        Long id, TipoCombustivel tipoCombustivel, BigDecimal quantidadeLitros, BigDecimal ultimoValorPorLitro
) {
    public static EstoqueCombustivelResponse de(EstoqueCombustivel e) {
        return new EstoqueCombustivelResponse(e.getId(), e.getTipoCombustivel(), e.getQuantidadeLitros(), e.getUltimoValorPorLitro());
    }
}