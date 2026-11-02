package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.safraDTO.CustosCombustivelManutencaoResponse;
import com.example.fazendalosardo.service.CustoCategoriaSafraService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/custo-categoria-safra")
@RequiredArgsConstructor
public class CustoCategoriaSafraController {

    private final CustoCategoriaSafraService custoCategoriaSafraService;

    @GetMapping("/{safraId}")
    public CustosCombustivelManutencaoResponse buscar(
            @PathVariable Long safraId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        return custoCategoriaSafraService.buscarPorSafra(safraId, dataInicio, dataFim);
    }
}