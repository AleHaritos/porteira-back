package com.example.fazendalosardo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "senha")
    private String senha;

    @Column(name = "numero", unique = true)
    private String numero;

    @Column(name = "admin")
    private Boolean admin = false;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cadastrado_por")
    private Usuario cadastradoPor;

    @OneToMany(mappedBy = "dono")
    private List<Fazenda> fazendas = new ArrayList<>();

    @OneToMany(mappedBy = "colaborador")
    private List<FazendaColaborador> participacoes = new ArrayList<>();

}
