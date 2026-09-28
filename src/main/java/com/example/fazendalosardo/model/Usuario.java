package com.example.fazendalosardo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usuarios")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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

    @OneToMany(mappedBy = "dono")
    private List<Fazenda> fazendas = new ArrayList<>();

    @OneToMany(mappedBy = "colaborador")
    private List<FazendaColaborador> participacoes = new ArrayList<>();

}
