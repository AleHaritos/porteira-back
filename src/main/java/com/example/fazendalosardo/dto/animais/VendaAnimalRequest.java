package com.example.fazendalosardo.dto.animais;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record VendaAnimalRequest(
        @NotEmpty List<Long> animalIds,
        @NotNull @Positive BigDecimal valor,
        @NotNull LocalDate data,
        String descricao,
        @NotNull Long fazendaId
) {}