package com.example.fazendalosardo.model;

import com.example.fazendalosardo.model.enums.TipoCuidado;
import com.example.fazendalosardo.model.enums.TipoVinculoCuidado;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "cuidado_animal")
@Getter
@Setter
public class CuidadoAnimal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoCuidado tipo;

    @Enumerated(EnumType.STRING)
    @Column(name = "vinculo_tipo", nullable = false)
    private TipoVinculoCuidado vinculoTipo;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false)
    private BigDecimal custo;

    @Column(nullable = false)
    private LocalDate data;

    @ManyToOne
    @JoinColumn(name = "animal_id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    private Animal animal;

    @ManyToOne
    @JoinColumn(name = "lote_id")
    private Lote lote;

    @ManyToOne
    @JoinColumn(name = "piquete_id", nullable = false)
    private Piquete piquete;

    @ManyToOne
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @ManyToOne
    @JoinColumn(name = "racao_id")
    private Racao racao;

    @Column(name = "quantidade_racao")
    private BigDecimal quantidadeRacao;
}