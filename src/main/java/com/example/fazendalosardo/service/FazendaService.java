package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.FazendaRequest;
import com.example.fazendalosardo.dto.FazendaResponse;
import com.example.fazendalosardo.dto.FazendaResumoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FazendaService {

    FazendaResponse criar(FazendaRequest request, String numeroUsuarioLogado);

    FazendaResponse buscarPorId(Long id);

    Page<FazendaResumoResponse> buscarFazendasPorColaborador(String numero, Pageable pageable);

    void removerColaborador(Long fazendaId, Long usuarioId);

    void adicionarColaborador(Long fazendaId, String numeroColaborador, String numeroUsuarioLogado);
}
