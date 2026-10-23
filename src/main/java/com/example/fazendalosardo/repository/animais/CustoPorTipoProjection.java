package com.example.fazendalosardo.repository.animais;

import com.example.fazendalosardo.model.enums.TipoCuidado;

import java.math.BigDecimal;

public interface CustoPorTipoProjection {
    TipoCuidado getTipo();
    BigDecimal getTotal();
}