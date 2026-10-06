package com.example.fazendalosardo.model;


import com.example.fazendalosardo.model.enums.TipoVeiculo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "veiculos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotBlank
    @Column(name = "placa", nullable = false, unique = true, length = 10)
    private String placa;

    @NotNull
    @Column(name = "data_compra", nullable = false)
    private LocalDate dataCompra;

    @NotNull
    @Positive
    @Column(name = "valor_compra", precision = 12, scale = 2, nullable = false)
    private BigDecimal valorCompra;

    @NotNull
    @Column(name = "ano", nullable = false)
    private Integer ano;

    @NotNull
    @PositiveOrZero
    @Column(name = "km_inicial", nullable = false)
    private Integer kmInicial;

    @Column(name = "observacao", length = 500)
    private String observacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoVeiculo tipo;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;;

}
