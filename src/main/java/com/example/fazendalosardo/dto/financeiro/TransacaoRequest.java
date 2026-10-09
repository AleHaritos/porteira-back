package com.example.fazendalosardo.dto.financeiro;

import com.example.fazendalosardo.model.enums.TipoTransacao;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoRequest(
        @NotNull LocalDate data,
        @NotNull BigDecimal valor,
        @NotNull TipoTransacao tipo,
        String descricao,
        String observacao,
        @NotNull Long fazendaId,
        Long negocioId,
        Long safraId
) {
}
