package com.example.fazendalosardo.dto.equipamento;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContaFornecedorRequest(
        @NotNull Long fornecedorId,
        @NotNull Long fazendaId,
        @NotBlank String descricao,
        @NotNull LocalDate data,
        @NotNull @Positive BigDecimal valor,
        Long negocioId,
        Long safraId,
        Long projetoId
) {
}