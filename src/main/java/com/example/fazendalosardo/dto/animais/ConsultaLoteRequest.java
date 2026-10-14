package com.example.fazendalosardo.dto.animais;

import com.example.fazendalosardo.model.enums.TipoConsulta;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConsultaLoteRequest(
        @NotNull Long loteId,
        @NotNull TipoConsulta tipo,
        @NotNull LocalDate data,
        @NotNull @Positive Integer quantidadeAnimais,
        @NotNull @Positive BigDecimal custo,
        String descricao,
        String observacao,
        @NotNull Long fazendaId,
        Long negocioId,
        Long safraId
) {
}
