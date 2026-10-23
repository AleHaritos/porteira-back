package com.example.fazendalosardo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "operacao_venda")
@Getter
@Setter
public class OperacaoVenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @Column(nullable = false)
    private LocalDate data;

    @Column(length = 255)
    private String descricao;

    @Column(name = "valor_venda", nullable = false)
    private BigDecimal valorVenda;

    @Column(name = "custo_total", nullable = false)
    private BigDecimal custoTotal;

    @Column(nullable = false)
    private BigDecimal lucro;

    @OneToMany(mappedBy = "operacaoVenda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OperacaoVendaItem> itens = new ArrayList<>();
}