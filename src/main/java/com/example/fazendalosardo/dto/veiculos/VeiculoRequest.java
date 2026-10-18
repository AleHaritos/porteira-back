package com.example.fazendalosardo.dto.veiculos;

import com.example.fazendalosardo.model.enums.TipoVeiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record VeiculoRequest(
        @NotBlank String nome,
        @NotBlank String placa,
        @NotNull LocalDate dataCompra,
        String observacao,
        @NotNull TipoVeiculo tipo,
        @NotNull Long fazendaId
) {
}