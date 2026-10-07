package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.safraDTO.TalhaoRequest;
import com.example.fazendalosardo.dto.safraDTO.TalhaoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TalhaoService {
    TalhaoResponse salvar(TalhaoRequest request);
    Page<TalhaoResponse> buscarPorSafra(Long safraId, Pageable pageable);
    List<TalhaoResponse> listarPorSafra(Long safraId);
    void desativar(Long id);
    void reativar(Long id);
}
