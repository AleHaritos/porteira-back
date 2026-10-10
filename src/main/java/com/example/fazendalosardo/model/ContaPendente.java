package com.example.fazendalosardo.model;

import com.example.fazendalosardo.model.enums.TipoTransacao;
import com.example.fazendalosardo.model.safras.Safra;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conta_pendente")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContaPendente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descricao;

    @Column(nullable = false)
    private BigDecimal valorTotal;

    @Column(nullable = false)
    private Integer numeroParcelas;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoTransacao tipo;

    @ManyToOne(optional = false)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @ManyToOne
    @JoinColumn(name = "negocio_id")
    private Negocio negocio;

    @ManyToOne
    @JoinColumn(name = "safra_id")
    private Safra safra;

    private String observacao;

    @Column(nullable = false)
    private boolean ativo = true;

    @OneToMany(mappedBy = "contaPendente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ParcelaContaPendente> parcelas = new ArrayList<>();

}