package com.example.fazendalosardo.model;

import com.example.fazendalosardo.model.enums.StatusPiquete;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "piquete")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Piquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(name = "area_hectares", nullable = false, precision = 10, scale = 2)
    private BigDecimal areaHectares;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPiquete status;

    @Column(nullable = false)
    private Boolean ativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fazenda_id", nullable = false)
    private Fazenda fazenda;

    @PrePersist
    public void prePersist() {
        if (ativo == null) {
            ativo = true;
        }
        if (status == null) {
            status = StatusPiquete.CRIA;
        }
    }
}