package com.example.fazendalosardo.repository.equipamento;

import com.example.fazendalosardo.model.Fornecedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    Page<Fornecedor> findByFazendaIdAndAtivoTrueOrderByNome(Long fazendaId, Pageable pageable);
    List<Fornecedor> findByFazendaIdAndAtivoTrueOrderByNome(Long fazendaId);
}