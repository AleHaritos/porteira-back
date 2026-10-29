package com.example.fazendalosardo.dto.calendario;

import com.example.fazendalosardo.model.EventoCalendario;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventoCalendarioResponse(
        Long id,
        String titulo,
        String descricao,
        LocalDate data,
        LocalTime horaInicio,
        LocalTime horaFim
) {
    public static EventoCalendarioResponse de(EventoCalendario e) {
        return new EventoCalendarioResponse(e.getId(), e.getTitulo(), e.getDescricao(), e.getData(),
                e.getHoraInicio(), e.getHoraFim());
    }
}