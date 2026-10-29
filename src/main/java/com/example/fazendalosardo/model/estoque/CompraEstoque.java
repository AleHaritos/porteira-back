package com.example.fazendalosardo.model.estoque;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "compras_estoque")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class CompraEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "produto_estoque_id", nullable = false)
    private ProdutoEstoque produto;

    @NotNull
    @Positive
    @Column(name = "quantidade", precision = 12, scale = 2, nullable = false)
    private BigDecimal quantidade;

    @NotNull
    @Positive
    @Column(name = "valor_unitario", precision = 12, scale = 2, nullable = false)
    private BigDecimal valorUnitario;

    @NotNull
    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "observacao", length = 500)
    private String observacao;
}