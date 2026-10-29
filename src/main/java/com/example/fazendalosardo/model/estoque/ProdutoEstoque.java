package com.example.fazendalosardo.model.estoque;

import com.example.fazendalosardo.model.Fazenda;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos_estoque")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ProdutoEstoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @NotBlank
    @Column(name = "nome", nullable = false, length = 150)
    private String nome;

    @NotNull
    @PositiveOrZero
    @Column(name = "valor", precision = 12, scale = 2, nullable = false)
    private BigDecimal valor = BigDecimal.ZERO;

    @NotNull
    @PositiveOrZero
    @Column(name = "quantidade", precision = 12, scale = 2, nullable = false)
    private BigDecimal quantidade = BigDecimal.ZERO;
}