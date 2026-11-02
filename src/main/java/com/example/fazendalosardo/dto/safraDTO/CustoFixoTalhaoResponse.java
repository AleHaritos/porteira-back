package com.example.fazendalosardo.dto.safraDTO;

import com.example.fazendalosardo.model.safras.CustoFixoTalhao;

import java.math.BigDecimal;

public record CustoFixoTalhaoResponse(
        Long id,
        Long talhaoId,
        String descricao,
        BigDecimal valor,
        boolean arrendo
) {
    public static CustoFixoTalhaoResponse de(CustoFixoTalhao custo) {
        return new CustoFixoTalhaoResponse(
                custo.getId(),
                custo.getTalhao().getId(),
                custo.getDescricao(),
                custo.getValor(),
                custo.isArrendo()
        );
    }
}