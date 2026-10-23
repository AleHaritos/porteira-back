package com.example.fazendalosardo.model;

import com.example.fazendalosardo.model.enums.SexoAnimal;
import com.example.fazendalosardo.model.enums.TipoAnimal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "animal")
@Getter
@Setter
public class Animal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoAnimal tipo;

    @Column(length = 255)
    private String descricao;

    @ManyToOne
    @JoinColumn(name = "lote_id")
    private Lote lote;

    @ManyToOne
    @JoinColumn(name = "piquete_id", nullable = false)
    private Piquete piquete;

    @ManyToOne
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @Column(nullable = false)
    private Boolean ativo;

    @Enumerated(EnumType.STRING)
    private SexoAnimal sexo;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "data_compra")
    private LocalDate dataCompra;

    @Column(name = "valor_compra")
    private BigDecimal valorCompra;

    private BigDecimal peso;

    private String brinco;

    private String raca;

    @PrePersist
    private void prePersist() {
        if (this.ativo == null) {
            this.ativo = true;
        }
    }

    @PostPersist
    private void gerarBrincoPadrao() {
        if (this.brinco == null || this.brinco.isBlank()) {
            this.brinco = String.valueOf(this.id);
        }
    }
}