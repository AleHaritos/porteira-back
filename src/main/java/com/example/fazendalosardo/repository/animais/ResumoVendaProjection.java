package com.example.fazendalosardo.repository.animais;

import java.math.BigDecimal;

public interface ResumoVendaProjection {
    BigDecimal getReceitaTotal();
    BigDecimal getCustoTotal();
    BigDecimal getLucroTotal();
}