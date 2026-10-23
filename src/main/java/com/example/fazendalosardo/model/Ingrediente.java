package com.example.fazendalosardo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "ingrediente")
@Getter
@Setter
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "valor_unitario", nullable = false)
    private BigDecimal valorUnitario;

    private String unidade;

    @Column(nullable = false)
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @PrePersist
    private void prePersist() {
        if (this.ativo == null) this.ativo = true;
    }
}