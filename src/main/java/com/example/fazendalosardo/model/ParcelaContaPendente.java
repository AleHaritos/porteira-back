package com.example.fazendalosardo.model;

import com.example.fazendalosardo.model.enums.StatusParcela;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "parcela_conta_pendente")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParcelaContaPendente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "conta_pendente_id", nullable = false)
    private ContaPendente contaPendente;

    @Column(nullable = false)
    private Integer numeroParcela;

    @Column(nullable = false)
    private BigDecimal valor;

    @Column(nullable = false)
    private LocalDate dataVencimento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusParcela status = StatusParcela.PENDENTE;

    private LocalDate dataBaixa;

    @OneToOne
    @JoinColumn(name = "transacao_id")
    private Transacao transacao;
}