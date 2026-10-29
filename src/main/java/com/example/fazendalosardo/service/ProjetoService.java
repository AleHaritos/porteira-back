package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.projeto.ProjetoRequest;
import com.example.fazendalosardo.dto.projeto.ProjetoResponse;
import com.example.fazendalosardo.dto.projeto.ProjetoUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjetoService {
    ProjetoResponse salvar(ProjetoRequest request, String numeroUsuarioLogado);
    Page<ProjetoResponse> buscarPorFazenda(Long fazendaId, Pageable pageable);
    List<ProjetoResponse> listarTodosPorFazenda(Long fazendaId);
    ProjetoResponse atualizar(Long id, ProjetoUpdateRequest request, String numeroUsuarioLogado);
    void desativar(Long id, String numeroUsuarioLogado);
    void reativar(Long id, String numeroUsuarioLogado);
}