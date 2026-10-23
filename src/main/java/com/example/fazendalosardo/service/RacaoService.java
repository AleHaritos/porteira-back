package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.animais.RacaoRequest;
import com.example.fazendalosardo.dto.animais.RacaoResponse;

import java.util.List;

public interface RacaoService {
    RacaoResponse salvar(RacaoRequest request, String numeroUsuarioLogado);
    RacaoResponse atualizar(Long id, RacaoRequest request, String numeroUsuarioLogado);
    List<RacaoResponse> listarPorFazenda(Long fazendaId, String numeroUsuarioLogado);
    void inativar(Long id, String numeroUsuarioLogado);
}