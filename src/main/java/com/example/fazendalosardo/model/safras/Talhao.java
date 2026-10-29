package com.example.fazendalosardo.model.safras;

import com.example.fazendalosardo.model.enums.TipoManejo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "talhoes")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Talhao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "nome", nullable = false)
    private String nome;

    @NotNull
    @Positive
    @Column(name = "area_hectares", precision = 12, scale = 2, nullable = false)
    private BigDecimal areaHectares;

    @Column(name = "localizacao", length = 255)
    private String localizacao;

    @Column(name = "observacao", length = 500)
    private String observacao;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status_talhao", length = 30, nullable = false)
    private TipoManejo statusTalhao = TipoManejo.PREPARO_DO_SOLO;

    @Column(nullable = false)
    private Boolean ativo = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "safra_id", nullable = false)
    private Safra safra;
}