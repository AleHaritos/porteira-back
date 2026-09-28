package com.example.fazendalosardo.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "fazenda")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Fazenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Positive
    @Column(name = "hectares", precision = 12, scale = 2, nullable = false)
    private BigDecimal hectares;

    @Column(name = "localizacao")
    private String localizacao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_dono", nullable = false)
    private Usuario dono;

    @OneToMany(mappedBy = "fazenda", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FazendaColaborador> colaboradores = new ArrayList<>();
}
