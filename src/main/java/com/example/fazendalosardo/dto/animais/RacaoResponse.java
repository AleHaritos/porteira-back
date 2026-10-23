package com.example.fazendalosardo.dto.animais;

import java.math.BigDecimal;
import java.util.List;

public record RacaoResponse(
        Long id,
        String nome,
        Boolean ativo,
        BigDecimal custoTotal,
        List<RacaoIngredienteResponse> ingredientes
) {}