package com.example.fazendalosardo.dto.financeiro;

import com.example.fazendalosardo.model.enums.TipoTransacao;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransacaoUpdateRequest(
        @NotNull LocalDate data,
        @NotNull BigDecimal valor,
        @NotNull TipoTransacao tipo,
        String descricao,
        String observacao,
        Long negocioId,
        Long safraId
) {
}
