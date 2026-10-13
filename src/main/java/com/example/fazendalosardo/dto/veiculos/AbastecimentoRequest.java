package com.example.fazendalosardo.dto.veiculos;

import com.example.fazendalosardo.model.enums.LocalAbastecimento;
import com.example.fazendalosardo.model.enums.TipoCombustivel;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AbastecimentoRequest(
        @NotNull Long veiculoId,
        @NotNull LocalDate data,
        @NotNull @Positive BigDecimal litros,
        BigDecimal valorPorLitro,
        @NotNull TipoCombustivel tipoCombustivel,
        @NotNull LocalAbastecimento localAbastecimento,
        Long postoId,
        @PositiveOrZero Integer kmAtual,
        String observacao,
        @NotNull Long fazendaId,
        Long negocioId,
        Long safraId
) {
}
