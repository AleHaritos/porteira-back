package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.safraDTO.ProdutoSafraRequest;
import com.example.fazendalosardo.dto.safraDTO.ProdutoSafraResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProdutoSafraService {
    ProdutoSafraResponse salvar(ProdutoSafraRequest request);
    Page<ProdutoSafraResponse> buscarPorSafra(Long safraId, Pageable pageable);
    List<ProdutoSafraResponse> listarPorSafra(Long safraId);
}
