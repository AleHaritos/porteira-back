package com.example.fazendalosardo.dto.equipamento;

import com.example.fazendalosardo.model.Equipamento;

import java.math.BigDecimal;
import java.time.LocalDate;

public record EquipamentoResponse(
        Long id,
        String nome,
        LocalDate dataCompra,
        BigDecimal valorCompra,
        String observacao,
        Boolean ativo
) {
    public static EquipamentoResponse de(Equipamento e) {
        return new EquipamentoResponse(e.getId(), e.getNome(), e.getDataCompra(), e.getValorCompra(), e.getObservacao(), e.getAtivo());
    }
}