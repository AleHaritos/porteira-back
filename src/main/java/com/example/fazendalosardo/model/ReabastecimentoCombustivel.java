package com.example.fazendalosardo.model;

import com.example.fazendalosardo.model.enums.TipoCombustivel;
import com.example.fazendalosardo.model.safras.Safra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "reabastecimentos_combustivel")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ReabastecimentoCombustivel {
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
    @Positive
    @Column(name = "litros", precision = 12, scale = 3, nullable = false)
    private BigDecimal litros;

    @NotNull
    @Positive
    @Column(name = "valor_por_litro", precision = 10, scale = 3, nullable = false)
    private BigDecimal valorPorLitro;

    @NotNull
    @Column(name = "data", nullable = false)
    private LocalDate data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "safra_id")
    private Safra safra;

    @Column(name = "observacao")
    private String observacao;
}