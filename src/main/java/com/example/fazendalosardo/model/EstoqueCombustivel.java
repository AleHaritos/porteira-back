package com.example.fazendalosardo.model;

import com.example.fazendalosardo.model.enums.TipoCombustivel;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "estoques_combustivel", uniqueConstraints = @UniqueConstraint(columnNames = {"fazenda_id", "tipo_combustivel"}))
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class EstoqueCombustivel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_combustivel", nullable = false, length = 20)
    private TipoCombustivel tipoCombustivel;

    @NotNull
    @PositiveOrZero
    @Column(name = "quantidade_litros", precision = 12, scale = 3, nullable = false)
    private BigDecimal quantidadeLitros = BigDecimal.ZERO;

    @Column(name = "ultimo_valor_por_litro", precision = 10, scale = 3)
    private BigDecimal ultimoValorPorLitro;
}