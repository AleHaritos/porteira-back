package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.animais.PiqueteRequest;
import com.example.fazendalosardo.dto.animais.PiqueteResponse;

import java.util.List;

public interface PiqueteService {
    PiqueteResponse salvar(PiqueteRequest request, String numeroUsuarioLogado);
    PiqueteResponse atualizar(Long id, PiqueteRequest request, String numeroUsuarioLogado);
    List<PiqueteResponse> listarPorFazenda(Long fazendaId, String numeroUsuarioLogado);
    void inativar(Long id, String numeroUsuarioLogado);
}