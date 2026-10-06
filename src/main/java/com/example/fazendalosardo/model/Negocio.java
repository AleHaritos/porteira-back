package com.example.fazendalosardo.model;

import com.example.fazendalosardo.model.enums.TipoNegocio;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "negocio")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Negocio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 30)
    private TipoNegocio tipo;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;
}