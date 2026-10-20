package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.safraDTO.CustoFixoTalhaoRequest;
import com.example.fazendalosardo.dto.safraDTO.CustoFixoTalhaoResponse;

import java.util.List;

public interface CustoFixoTalhaoService {
    CustoFixoTalhaoResponse salvar(CustoFixoTalhaoRequest request);

    List<CustoFixoTalhaoResponse> listarPorTalhao(Long talhaoId);

    void excluir(Long id);
}
