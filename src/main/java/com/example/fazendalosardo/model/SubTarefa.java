package com.example.fazendalosardo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "subtarefas")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class SubTarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tarefa_id", nullable = false)
    private Tarefa tarefa;

    @NotBlank
    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "concluida", nullable = false)
    private Boolean concluida = false;

    @Column(name = "ordem", nullable = false)
    private Integer ordem = 0;
}