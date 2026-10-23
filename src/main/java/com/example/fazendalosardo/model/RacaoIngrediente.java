package com.example.fazendalosardo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "racao_ingrediente")
@Getter
@Setter
public class RacaoIngrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "racao_id", nullable = false)
    private Racao racao;

    @ManyToOne
    @JoinColumn(name = "ingrediente_id", nullable = false)
    private Ingrediente ingrediente;

    @Column(nullable = false)
    private BigDecimal quantidade;
}