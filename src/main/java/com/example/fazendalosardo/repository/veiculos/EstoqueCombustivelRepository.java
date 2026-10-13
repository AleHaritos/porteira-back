package com.example.fazendalosardo.repository.veiculos;

import com.example.fazendalosardo.model.EstoqueCombustivel;
import com.example.fazendalosardo.model.enums.TipoCombustivel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EstoqueCombustivelRepository extends JpaRepository<EstoqueCombustivel, Long> {
    Optional<EstoqueCombustivel> findByFazendaIdAndTipoCombustivel(Long fazendaId, TipoCombustivel tipoCombustivel);
    List<EstoqueCombustivel> findByFazendaId(Long fazendaId);
}