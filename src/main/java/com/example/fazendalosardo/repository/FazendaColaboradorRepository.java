package com.example.fazendalosardo.repository;

import com.example.fazendalosardo.model.FazendaColaborador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FazendaColaboradorRepository extends JpaRepository<FazendaColaborador, Long> {

    List<FazendaColaborador> findByFazendaId(Long fazendaId);

    List<FazendaColaborador> findByColaboradorId(Long usuarioId);

    Optional<FazendaColaborador> findByFazendaIdAndColaboradorId(Long fazendaId, Long usuarioId);

    boolean existsByFazendaIdAndColaboradorId(Long fazendaId, Long usuarioId);

    void deleteByFazendaIdAndColaboradorId(Long fazendaId, Long usuarioId);

}
