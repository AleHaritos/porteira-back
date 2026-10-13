package com.example.fazendalosardo.repository.equipamento;

import com.example.fazendalosardo.model.Equipamento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {
    Page<Equipamento> findByFazendaIdAndAtivoTrueOrderByNome(Long fazendaId, Pageable pageable);
}