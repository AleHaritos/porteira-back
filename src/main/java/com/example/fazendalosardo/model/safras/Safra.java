package com.example.fazendalosardo.model.safras;

import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.enums.StatusSafra;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "safras")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Safra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotBlank
    @Column(name = "cultura", nullable = false)
    private String cultura;

    @NotBlank
    @Column(name = "ano_agricola", nullable = false, length = 9)
    private String anoAgricola;

    @NotNull
    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "previsao_fim")
    private LocalDate previsaoFim;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StatusSafra status = StatusSafra.PLANEJAMENTO;

    @NotNull
    @Positive
    @Column(name = "area_total", precision = 12, scale = 2, nullable = false)
    private BigDecimal areaTotal;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;
}
