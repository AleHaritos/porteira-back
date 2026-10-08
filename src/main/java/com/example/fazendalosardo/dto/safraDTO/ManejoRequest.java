package com.example.fazendalosardo.dto.safraDTO;

import com.example.fazendalosardo.model.enums.TipoManejo;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ManejoRequest(
        @NotNull TipoManejo tipo,
        @NotNull LocalDate data,
        String descricao,
        String observacoes,
        @NotNull Long talhaoId,
        List<ItemManejoRequest> itens
) {
}
