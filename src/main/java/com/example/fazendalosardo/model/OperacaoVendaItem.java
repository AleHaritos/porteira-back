package com.example.fazendalosardo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Table(name = "operacao_venda_item")
@Getter
@Setter
public class OperacaoVendaItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "operacao_venda_id", nullable = false)
    private OperacaoVenda operacaoVenda;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Animal animal;

    @Column(name = "custo_vacinacao", nullable = false)
    private BigDecimal custoVacinacao;

    @Column(name = "custo_alimentacao", nullable = false)
    private BigDecimal custoAlimentacao;

    @Column(name = "valor_compra", nullable = false)
    private BigDecimal valorCompra;

    @Column(name = "custo_total", nullable = false)
    private BigDecimal custoTotal;
}