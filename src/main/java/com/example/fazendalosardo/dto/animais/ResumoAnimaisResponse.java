package com.example.fazendalosardo.dto.animais;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ResumoAnimaisResponse(
        BigDecimal gastoAlimentacao,
        BigDecimal gastoVacinacao,
        BigDecimal gastoTotal,
        BigDecimal receitaVendas,
        BigDecimal custoVendas,
        BigDecimal lucroVendas,
        List<OperacaoVendaResumoItem> operacoesVenda,
        List<CuidadoAnimalResumoItem> cuidados
) {
    public record OperacaoVendaResumoItem(
            Long id,
            LocalDate data,
            String descricao,
            BigDecimal valorVenda,
            BigDecimal custoTotal,
            BigDecimal lucro,
            int quantidadeAnimais
    ) {
    }

    public record CuidadoAnimalResumoItem(
            Long id,
            String tipo,
            LocalDate data,
            String descricao,
            BigDecimal custo,
            String piqueteNome,
            String destino
    ) {
    }
}