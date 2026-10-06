package com.example.fazendalosardo.dto.safraDTO;

import com.example.fazendalosardo.model.enums.StatusSafra;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record SafraRequest(
        @NotBlank String nome,
        @NotBlank String cultura,
        @NotBlank String anoAgricola,
        StatusSafra status,
        @NotNull @Positive BigDecimal areaTotal,
        String observacoes,
        @NotNull Long fazendaId
) {
}
