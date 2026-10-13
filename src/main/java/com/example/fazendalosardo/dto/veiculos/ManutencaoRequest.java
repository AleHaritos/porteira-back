package com.example.fazendalosardo.dto.veiculos;

import com.example.fazendalosardo.model.enums.TipoManutencao;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ManutencaoRequest(
        @NotNull Long veiculoId,
        @NotNull TipoManutencao tipo,
        String descricao,
        @NotNull LocalDate data,
        @NotNull @Positive BigDecimal valor,
        @PositiveOrZero Integer kmAtual,
        String observacao,
        @NotNull Long fazendaId,
        Long negocioId,
        Long safraId
) {
}
