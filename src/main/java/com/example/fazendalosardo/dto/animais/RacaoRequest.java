package com.example.fazendalosardo.dto.animais;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public record RacaoRequest(
        @NotBlank String nome,
        @NotNull Long fazendaId,
        @NotEmpty @Valid List<ItemIngrediente> ingredientes
) {
    public record ItemIngrediente(
            @NotNull Long ingredienteId,
            @NotNull @Positive BigDecimal quantidade
    ) {}
}