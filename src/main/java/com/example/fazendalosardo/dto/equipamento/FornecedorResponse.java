package com.example.fazendalosardo.dto.equipamento;

import com.example.fazendalosardo.model.Fornecedor;

import java.math.BigDecimal;

public record FornecedorResponse(
        Long id,
        String nome,
        String especialidade,
        BigDecimal saldoDevedor,
        String observacao,
        Boolean ativo
) {
    public static FornecedorResponse de(Fornecedor f) {
        return new FornecedorResponse(f.getId(), f.getNome(), f.getEspecialidade(), f.getSaldoDevedor(), f.getObservacao(), f.getAtivo());
    }
}