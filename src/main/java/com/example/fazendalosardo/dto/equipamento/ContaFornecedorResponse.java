package com.example.fazendalosardo.dto.equipamento;

import com.example.fazendalosardo.model.ContaFornecedor;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ContaFornecedorResponse(
        Long id,
        Long fornecedorId,
        String fornecedorNome,
        String descricao,
        LocalDate data,
        BigDecimal valor,
        String vinculoNome,
        String statusPagamento,
        LocalDate dataBaixa
) {
    public static ContaFornecedorResponse de(ContaFornecedor c) {
        String vinculo = c.getNegocio() != null ? c.getNegocio().getNome()
                : c.getSafra() != null ? c.getSafra().getNome() : null;
        return new ContaFornecedorResponse(c.getId(), c.getFornecedor().getId(), c.getFornecedor().getNome(),
                c.getDescricao(), c.getData(), c.getValor(), vinculo,
                c.getStatusPagamento().name(), c.getDataBaixa());
    }
}