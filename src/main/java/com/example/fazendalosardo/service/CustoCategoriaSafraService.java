package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.safraDTO.CustosCombustivelManutencaoResponse;

public interface CustoCategoriaSafraService {
    CustosCombustivelManutencaoResponse buscarPorSafra(Long safraId);
}