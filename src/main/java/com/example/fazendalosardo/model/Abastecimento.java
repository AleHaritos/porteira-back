package com.example.fazendalosardo.model;

import com.example.fazendalosardo.model.enums.LocalAbastecimento;
import com.example.fazendalosardo.model.enums.TipoCombustivel;
import com.example.fazendalosardo.model.safras.Safra;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "abastecimentos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Abastecimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    @NotNull
    @Column(name = "data", nullable = false)
    private LocalDate data;

    @NotNull
    @Positive
    @Column(name = "litros", precision = 10, scale = 3, nullable = false)
    private BigDecimal litros;

    @Column(name = "valor_por_litro", precision = 10, scale = 3)
    private BigDecimal valorPorLitro;

    @Column(name = "valor_total", precision = 12, scale = 2)
    private BigDecimal valorTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posto_id")
    private Posto posto;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_combustivel", nullable = false, length = 20)
    private TipoCombustivel tipoCombustivel;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "local_abastecimento", nullable = false, length = 20)
    private LocalAbastecimento localAbastecimento;

    @PositiveOrZero
    @Column(name = "km_atual")
    private Integer kmAtual;

    @Column(name = "observacao", length = 500)
    private String observacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "safra_id")
    private Safra safra;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transacao_id")
    private Transacao transacao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;
}
