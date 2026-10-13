package com.example.fazendalosardo.dto.veiculos;

import com.example.fazendalosardo.model.enums.TipoVeiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VeiculoRequest(
        @NotBlank String nome,
        @NotBlank String placa,
        @NotNull LocalDate dataCompra,
        @NotNull @Positive BigDecimal valorCompra,
        @NotNull Integer ano,
        @NotNull @PositiveOrZero Integer kmInicial,
        String observacao,
        @NotNull TipoVeiculo tipo,
        @NotNull Long fazendaId
) {
}
