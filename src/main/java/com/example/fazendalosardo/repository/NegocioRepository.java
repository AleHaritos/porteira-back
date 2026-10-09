package com.example.fazendalosardo.repository;

import com.example.fazendalosardo.model.Negocio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NegocioRepository extends JpaRepository<Negocio, Long> {
    Page<Negocio> findByFazendaIdAndAtivoTrue(Long fazendaId, Pageable pageable);
    List<Negocio> findByFazendaIdAndAtivoTrueOrderByNomeAsc(Long fazendaId);
}
