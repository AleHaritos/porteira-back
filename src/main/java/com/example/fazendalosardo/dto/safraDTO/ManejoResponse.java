package com.example.fazendalosardo.dto.safraDTO;

import com.example.fazendalosardo.model.enums.TipoManejo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ManejoResponse(
        Long id,
        TipoManejo tipo,
        LocalDate data,
        String descricao,
        String observacoes,
        Long talhaoId,
        String talhaoNome,
        Long produtoSafraId,
        String produtoSafraNome,
        BigDecimal quantidadeProduto,
        List<ItemManejoResponse> itens,
        BigDecimal custoTotal
) {
}
