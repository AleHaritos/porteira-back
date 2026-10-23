package com.example.fazendalosardo.dto.animais;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OperacaoVendaResponse(
        Long id,
        LocalDate data,
        String descricao,
        BigDecimal valorVenda,
        BigDecimal custoTotal,
        BigDecimal lucro,
        List<OperacaoVendaItemResponse> itens
) {}