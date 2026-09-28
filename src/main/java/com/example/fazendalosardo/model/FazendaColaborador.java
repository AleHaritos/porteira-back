package com.example.fazendalosardo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "fazenda_colaborador")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FazendaColaborador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario colaborador;

    @Column(name = "adicionado_em", nullable = false, updatable = false)
    private LocalDateTime adicionadoEm;

    @PrePersist
    protected void aoCriar() {
        this.adicionadoEm = LocalDateTime.now();
    }
}