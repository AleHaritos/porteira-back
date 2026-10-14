package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.animais.LoteRequest;
import com.example.fazendalosardo.dto.animais.LoteResponse;

import java.util.List;

public interface LoteService {
    LoteResponse salvar(LoteRequest request, String numeroUsuarioLogado);
    LoteResponse atualizar(Long id, LoteRequest request, String numeroUsuarioLogado);
    List<LoteResponse> listarPorFazenda(Long fazendaId, String numeroUsuarioLogado);
    void inativar(Long id, String numeroUsuarioLogado);
}