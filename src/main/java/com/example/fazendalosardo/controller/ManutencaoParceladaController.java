package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.veiculos.ManutencaoParceladaRequest;
import com.example.fazendalosardo.dto.veiculos.ManutencaoParceladaResponse;
import com.example.fazendalosardo.dto.veiculos.ParcelaManutencaoResponse;
import com.example.fazendalosardo.model.enums.StatusParcela;
import com.example.fazendalosardo.service.ManutencaoParceladaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/manutencao-parcelada")
@RequiredArgsConstructor
public class ManutencaoParceladaController {

    private final ManutencaoParceladaService manutencaoParceladaService;

    @PostMapping
    public ResponseEntity<ManutencaoParceladaResponse> salvar(@Valid @RequestBody ManutencaoParceladaRequest request,
                                                              Authentication authentication) {
        return ResponseEntity.ok(manutencaoParceladaService.salvar(request, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}/parcelas")
    public ResponseEntity<Page<ParcelaManutencaoResponse>> buscarParcelas(
            @PathVariable Long fazendaId,
            @RequestParam(required = false) StatusParcela status,
            Pageable pageable,
            Authentication authentication) {
        return ResponseEntity.ok(manutencaoParceladaService.buscarParcelasPorFazenda(fazendaId, status, pageable, authentication.getName()));
    }

    @PatchMapping("/parcela/{parcelaId}/baixa")
    public ResponseEntity<ParcelaManutencaoResponse> darBaixa(
            @PathVariable Long parcelaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataBaixa,
            Authentication authentication) {
        return ResponseEntity.ok(manutencaoParceladaService.darBaixa(parcelaId, dataBaixa, authentication.getName()));
    }
}