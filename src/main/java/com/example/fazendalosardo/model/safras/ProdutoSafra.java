package com.example.fazendalosardo.model.safras;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos_safra")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class ProdutoSafra {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Positive
    @Column(name = "custo", precision = 12, scale = 2, nullable = false)
    private BigDecimal custo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "safra_id", nullable = false)
    private Safra safra;
}