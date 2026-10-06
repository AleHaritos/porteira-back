package com.example.fazendalosardo.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "equipamentos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Column(name = "data_compra", nullable = false)
    private LocalDate dataCompra;

    @NotNull
    @Positive
    @Column(name = "valor_compra", precision = 12, scale = 2, nullable = false)
    private BigDecimal valorCompra;

    @Column(name = "observacao", length = 500)
    private String observacao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;
}
