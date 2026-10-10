package com.example.fazendalosardo.dto.financeiro;

import java.math.BigDecimal;

public record ResumoFinanceiroResponse(BigDecimal totalReceitas, BigDecimal totalGastos, BigDecimal saldo) {
    public ResumoFinanceiroResponse(BigDecimal totalReceitas, BigDecimal totalGastos) {
        this(totalReceitas, totalGastos, totalReceitas.subtract(totalGastos));
    }
}