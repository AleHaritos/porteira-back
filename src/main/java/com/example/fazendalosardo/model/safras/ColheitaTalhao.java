package com.example.fazendalosardo.model.safras;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "colheitas_talhao")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ColheitaTalhao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "descricao", length = 255)
    private String descricao;

    @NotNull
    @PositiveOrZero
    @Column(name = "quantidade_colhida", precision = 12, scale = 2, nullable = false)
    private BigDecimal quantidadeColhida;

    @NotNull
    @PositiveOrZero
    @Column(name = "valor_unitario", precision = 12, scale = 2, nullable = false)
    private BigDecimal valorUnitario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "talhao_id", nullable = false)
    private Talhao talhao;
}