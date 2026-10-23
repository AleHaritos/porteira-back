package com.example.fazendalosardo.repository.animais;

import com.example.fazendalosardo.model.OperacaoVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OperacaoVendaRepository extends JpaRepository<OperacaoVenda, Long> {

    @Query("""
        SELECT o FROM OperacaoVenda o
        LEFT JOIN FETCH o.itens i
        LEFT JOIN FETCH i.animal
        WHERE o.fazenda.id = :fazendaId
        ORDER BY o.data DESC, o.id DESC
        """)
    List<OperacaoVenda> buscarPorFazenda(@Param("fazendaId") Long fazendaId);
}