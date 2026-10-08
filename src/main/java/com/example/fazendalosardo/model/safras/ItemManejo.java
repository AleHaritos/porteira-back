package com.example.fazendalosardo.model.safras;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "itens_manejo")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ItemManejo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manejo_id", nullable = false)
    private Manejo manejo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_safra_id", nullable = false)
    private ProdutoSafra produtoSafra;

    @Column(nullable = false)
    private BigDecimal quantidade;
}