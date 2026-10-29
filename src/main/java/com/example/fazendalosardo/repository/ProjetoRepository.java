package com.example.fazendalosardo.repository;

import com.example.fazendalosardo.model.Projeto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjetoRepository extends JpaRepository<Projeto, Long> {
    Page<Projeto> findByFazendaIdAndAtivoTrue(Long fazendaId, Pageable pageable);
    List<Projeto> findByFazendaIdAndAtivoTrueOrderByNomeAsc(Long fazendaId);
}