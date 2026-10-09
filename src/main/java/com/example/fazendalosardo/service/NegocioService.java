package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.negocio.NegocioRequest;
import com.example.fazendalosardo.dto.negocio.NegocioResponse;
import com.example.fazendalosardo.dto.negocio.NegocioUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NegocioService {
    NegocioResponse salvar(NegocioRequest request, String numeroUsuarioLogado);
    Page<NegocioResponse> buscarPorFazenda(Long fazendaId, Pageable pageable);
    List<NegocioResponse> listarTodosPorFazenda(Long fazendaId);
    NegocioResponse atualizar(Long id, NegocioUpdateRequest request, String numeroUsuarioLogado);
    void desativar(Long id, String numeroUsuarioLogado);
    void reativar(Long id, String numeroUsuarioLogado);
}
