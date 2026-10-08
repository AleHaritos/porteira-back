package com.example.fazendalosardo.model.safras;

import com.example.fazendalosardo.model.enums.TipoManejo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "manejos")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Manejo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoManejo tipo;

    @NotNull
    @Column(name = "data", nullable = false)
    private LocalDate data;

    @Column(name = "descricao", length = 500)
    private String descricao;

    @Column(name = "observacoes", length = 500)
    private String observacoes;

    @OneToMany(mappedBy = "manejo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemManejo> itens = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "talhao_id", nullable = false)
    private Talhao talhao;

}