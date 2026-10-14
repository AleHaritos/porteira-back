package com.example.fazendalosardo.dto.animais;

import com.example.fazendalosardo.model.enums.TipoConsulta;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ConsultaLoteResponse(
        Long id,
        Long loteId,
        String loteNome,
        TipoConsulta tipo,
        LocalDate data,
        Integer quantidadeAnimais,
        BigDecimal custo,
        String descricao,
        String observacao,
        String negocioNome,
        String safraNome
) {
}
