package com.example.fazendalosardo.dto.veiculos;

import com.example.fazendalosardo.model.Veiculo;
import com.example.fazendalosardo.model.enums.TipoVeiculo;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VeiculoResponse(
        Long id,
        String nome,
        String placa,
        LocalDate dataCompra,
        BigDecimal valorCompra,
        Integer ano,
        Integer kmInicial,
        String observacao,
        TipoVeiculo tipo,
        Boolean ativo
) {
    public static VeiculoResponse de(Veiculo v) {
        return new VeiculoResponse(v.getId(), v.getNome(), v.getPlaca(), v.getDataCompra(),
                v.getValorCompra(), v.getAno(), v.getKmInicial(), v.getObservacao(), v.getTipo(), v.getAtivo());
    }
}