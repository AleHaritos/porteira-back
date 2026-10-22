package com.example.fazendalosardo.dto.animais;

import com.example.fazendalosardo.model.enums.TipoCuidado;
import com.example.fazendalosardo.model.enums.TipoVinculoCuidado;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CuidadoAnimalResponse(
        Long id,
        TipoCuidado tipo,
        TipoVinculoCuidado vinculoTipo,
        String descricao,
        BigDecimal custo,
        LocalDate data,
        Long animalId,
        String animalTipo,
        String animalDescricao,
        Long loteId,
        String loteNome,
        Long piqueteId,
        String piqueteNome,
        Long negocioId,
        String negocioNome,
        Long safraId,
        String safraNome
) {}