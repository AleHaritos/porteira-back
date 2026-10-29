package com.example.fazendalosardo.dto.calendario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventoCalendarioRequest(
        @NotNull Long fazendaId,
        @NotBlank String titulo,
        String descricao,
        @NotNull LocalDate data,
        LocalTime horaInicio,
        LocalTime horaFim
) {
}