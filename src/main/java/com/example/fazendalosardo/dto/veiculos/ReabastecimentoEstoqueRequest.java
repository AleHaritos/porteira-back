package com.example.fazendalosardo.dto.veiculos;

import com.example.fazendalosardo.model.enums.TipoCombustivel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ReabastecimentoEstoqueRequest(
        @NotNull Long fazendaId,
        @NotNull TipoCombustivel tipoCombustivel,
        @NotNull @Positive BigDecimal litros,
        @NotNull @Positive BigDecimal valorPorLitro,
        @NotNull LocalDate data,
        Long negocioId,
        Long safraId,
        String observacao
) {
}
