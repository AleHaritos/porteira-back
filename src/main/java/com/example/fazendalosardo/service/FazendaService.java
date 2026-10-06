package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.fazendaDTO.FazendaRequest;
import com.example.fazendalosardo.dto.fazendaDTO.FazendaResponse;
import com.example.fazendalosardo.dto.fazendaDTO.FazendaResumoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FazendaService {

    FazendaResponse criar(FazendaRequest request, String numeroUsuarioLogado);

    FazendaResponse buscarPorId(Long id);

    Page<FazendaResumoResponse> buscarFazendasPorColaborador(String numero, Pageable pageable);

    void removerColaborador(Long fazendaId, Long usuarioId);

    void adicionarColaborador(Long fazendaId, String numeroColaborador, String numeroUsuarioLogado);
}
