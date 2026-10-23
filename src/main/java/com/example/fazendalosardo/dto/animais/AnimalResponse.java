package com.example.fazendalosardo.dto.animais;

import com.example.fazendalosardo.model.enums.SexoAnimal;
import com.example.fazendalosardo.model.enums.TipoAnimal;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AnimalResponse(
        Long id,
        TipoAnimal tipo,
        Long loteId,
        String loteNome,
        Long piqueteId,
        String piqueteNome,
        String descricao,
        Boolean ativo,
        SexoAnimal sexo,
        LocalDate dataNascimento,
        Integer idadeMeses,
        LocalDate dataCompra,
        BigDecimal valorCompra,
        BigDecimal peso,
        String brinco,
        String raca
) {}