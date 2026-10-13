package com.example.fazendalosardo.repository.equipamento;

import com.example.fazendalosardo.model.Fornecedor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FornecedorRepository extends JpaRepository<Fornecedor, Long> {
    List<Fornecedor> findByFazendaIdAndAtivoTrueOrderByNome(Long fazendaId);
}