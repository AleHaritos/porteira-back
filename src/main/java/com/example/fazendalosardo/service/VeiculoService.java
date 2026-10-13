package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.veiculos.VeiculoRequest;
import com.example.fazendalosardo.dto.veiculos.VeiculoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VeiculoService {
    VeiculoResponse salvar(VeiculoRequest request, String numeroUsuarioLogado);
    VeiculoResponse atualizar(Long id, VeiculoRequest request, String numeroUsuarioLogado);
    Page<VeiculoResponse> buscarPorFazenda(Long fazendaId, Pageable pageable, String numeroUsuarioLogado);
    VeiculoResponse buscarPorId(Long id, String numeroUsuarioLogado);
    void inativar(Long id, String numeroUsuarioLogado);
}