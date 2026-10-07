package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.safraDTO.ManejoRequest;
import com.example.fazendalosardo.dto.safraDTO.ManejoResponse;
import com.example.fazendalosardo.dto.safraDTO.ManejoUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ManejoService {
    ManejoResponse salvar(ManejoRequest request);
    Page<ManejoResponse> buscarPorTalhao(Long talhaoId, Pageable pageable);
    List<ManejoResponse> listarPorTalhao(Long talhaoId);
    ManejoResponse atualizar(Long id, ManejoUpdateRequest request);
    void excluir(Long id);
}
