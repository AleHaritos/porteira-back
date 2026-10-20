package com.example.fazendalosardo.dto.safraDTO;

import com.example.fazendalosardo.model.safras.CustoFixoTalhao;

import java.math.BigDecimal;

public record CustoFixoTalhaoResponse(
        Long id,
        Long talhaoId,
        String descricao,
        BigDecimal valor
) {
    public static CustoFixoTalhaoResponse de(CustoFixoTalhao c) {
        return new CustoFixoTalhaoResponse(c.getId(), c.getTalhao().getId(), c.getDescricao(), c.getValor());
    }
}