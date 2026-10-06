package com.example.fazendalosardo.dto.safraDTO;

import com.example.fazendalosardo.model.enums.StatusSafra;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SafraResponse(
        Long id,
        String nome,
        String cultura,
        String anoAgricola,
        LocalDate dataInicio,
        LocalDate previsaoFim,
        StatusSafra status,
        BigDecimal areaTotal,
        String observacoes,
        Long fazendaId,
        String fazendaNome
) {
}
