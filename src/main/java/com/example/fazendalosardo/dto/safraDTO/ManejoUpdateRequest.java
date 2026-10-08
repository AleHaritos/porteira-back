package com.example.fazendalosardo.dto.safraDTO;

import com.example.fazendalosardo.model.enums.TipoManejo;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ManejoUpdateRequest(
        @NotNull TipoManejo tipo,
        @NotNull LocalDate data,
        String descricao,
        String observacoes,
        List<ItemManejoRequest> itens
) {
}
