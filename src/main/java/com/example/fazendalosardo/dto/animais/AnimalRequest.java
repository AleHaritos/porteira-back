package com.example.fazendalosardo.dto.animais;

import com.example.fazendalosardo.model.enums.SexoAnimal;
import com.example.fazendalosardo.model.enums.TipoAnimal;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AnimalRequest(
        @NotNull TipoAnimal tipo,
        Long loteId,
        @NotNull Long piqueteId,
        @NotNull Long fazendaId,
        String descricao,
        SexoAnimal sexo,
        LocalDate dataNascimento,
        LocalDate dataCompra,
        BigDecimal valorCompra,
        BigDecimal peso,
        String brinco,
        String raca
) {}