package com.example.fazendalosardo.dto.animais;

import com.example.fazendalosardo.model.enums.TipoCuidado;
import com.example.fazendalosardo.model.enums.TipoVinculoCuidado;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CuidadoAnimalRequest(
        @NotNull TipoCuidado tipo,
        @NotNull TipoVinculoCuidado vinculoTipo,
        String descricao,
        @NotNull @Positive BigDecimal custo,
        @NotNull LocalDate data,
        Long animalId,
        Long loteId,
        @NotNull Long piqueteId,
        @NotNull Long fazendaId,
        Long racaoId,
        BigDecimal quantidadeRacao
) {}