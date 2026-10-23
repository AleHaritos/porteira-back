package com.example.fazendalosardo.dto.animais;

import com.example.fazendalosardo.model.enums.TipoCuidado;

import java.math.BigDecimal;
import java.time.LocalDate;

public record HistoricoCuidadoAnimalResponse(
        Long id,
        TipoCuidado tipo,
        String origem,
        String descricao,
        LocalDate data,
        BigDecimal custoTotalEvento,
        BigDecimal custoAtribuido
) {}