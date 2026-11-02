package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.safraDTO.CustosCombustivelManutencaoResponse;

import java.time.LocalDate;

public interface CustoCategoriaSafraService {
    CustosCombustivelManutencaoResponse buscarPorSafra(Long safraId, LocalDate dataInicio, LocalDate dataFim);
}