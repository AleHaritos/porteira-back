package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.animais.IngredienteRequest;
import com.example.fazendalosardo.dto.animais.IngredienteResponse;

import java.util.List;

public interface IngredienteService {
    IngredienteResponse salvar(IngredienteRequest request, String numeroUsuarioLogado);
    IngredienteResponse atualizar(Long id, IngredienteRequest request, String numeroUsuarioLogado);
    List<IngredienteResponse> listarPorFazenda(Long fazendaId, String numeroUsuarioLogado);
    void inativar(Long id, String numeroUsuarioLogado);
}