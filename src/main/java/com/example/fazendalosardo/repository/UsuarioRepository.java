package com.example.fazendalosardo.repository;

import com.example.fazendalosardo.model.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByNumero(String numero);

    Page<Usuario> findByCadastradoPorId(Long adminId, Pageable pageable);

    Boolean existsByNumero(String numero);
}
