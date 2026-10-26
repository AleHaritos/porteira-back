package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.equipamento.FornecedorDebitoResponse;
import com.example.fazendalosardo.model.enums.StatusParcela;
import com.example.fazendalosardo.service.FornecedorDebitoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/fornecedor-debito")
@RequiredArgsConstructor
public class FornecedorDebitoController {

    private final FornecedorDebitoService fornecedorDebitoService;

    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<Page<FornecedorDebitoResponse>> listarPorFornecedorEStatus(
            @PathVariable Long fornecedorId,
            @RequestParam StatusParcela status,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @PageableDefault(size = 10) Pageable pageable,
            Authentication authentication) {
        return ResponseEntity.ok(fornecedorDebitoService.listarPorFornecedorEStatus(fornecedorId, status, dataInicio, dataFim, pageable, authentication.getName()));
    }
}