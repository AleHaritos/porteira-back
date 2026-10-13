package com.example.fazendalosardo.dto.veiculos;

import com.example.fazendalosardo.model.Posto;

import java.math.BigDecimal;

public record PostoResponse(Long id, String nome, BigDecimal saldoDevedor, Boolean ativo, String observacao) {
    public static PostoResponse de(Posto p) {
        return new PostoResponse(p.getId(), p.getNome(), p.getSaldoDevedor(), p.getAtivo(), p.getObservacao());
    }
}