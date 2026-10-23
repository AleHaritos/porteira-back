package com.example.fazendalosardo.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "racao")
@Getter
@Setter
public class Racao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private Boolean ativo;

    @ManyToOne
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @OneToMany(mappedBy = "racao", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RacaoIngrediente> ingredientes = new ArrayList<>();

    @PrePersist
    private void prePersist() {
        if (this.ativo == null) this.ativo = true;
    }
}