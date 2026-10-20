package com.example.fazendalosardo.dto.safraDTO;

import com.example.fazendalosardo.model.safras.ColheitaTalhao;

import java.math.BigDecimal;

public record ColheitaTalhaoResponse(
        Long id,
        Long talhaoId,
        String descricao,
        BigDecimal quantidadeColhida,
        BigDecimal valorUnitario
) {
    public static ColheitaTalhaoResponse de(ColheitaTalhao c) {
        return new ColheitaTalhaoResponse(c.getId(), c.getTalhao().getId(), c.getDescricao(),
                c.getQuantidadeColhida(), c.getValorUnitario());
    }
}
