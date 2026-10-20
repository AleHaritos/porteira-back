package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.safraDTO.CustosCombustivelManutencaoResponse;
import com.example.fazendalosardo.service.CustoCategoriaSafraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/custo-categoria-safra")
@RequiredArgsConstructor
public class CustoCategoriaSafraController {

    private final CustoCategoriaSafraService custoCategoriaSafraService;

    @GetMapping("/{safraId}")
    public CustosCombustivelManutencaoResponse buscar(@PathVariable Long safraId) {
        return custoCategoriaSafraService.buscarPorSafra(safraId);
    }
}