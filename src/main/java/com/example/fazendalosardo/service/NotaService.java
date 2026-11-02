package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.NotaRequest;
import com.example.fazendalosardo.dto.NotaResponse;

import java.util.List;

public interface NotaService {
    NotaResponse salvar(NotaRequest request, String numeroUsuarioLogado);
    NotaResponse atualizar(Long id, NotaRequest request, String numeroUsuarioLogado);
    void excluir(Long id, String numeroUsuarioLogado);
    List<NotaResponse> buscarPorFazenda(Long fazendaId, String numeroUsuarioLogado);
}