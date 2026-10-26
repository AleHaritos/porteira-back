package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.equipamento.ManutencaoEquipamentoRequest;
import com.example.fazendalosardo.dto.equipamento.ManutencaoEquipamentoResponse;
import com.example.fazendalosardo.model.enums.StatusParcela;
import com.example.fazendalosardo.service.ManutencaoEquipamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/manutencao-equipamento")
@RequiredArgsConstructor
public class ManutencaoEquipamentoController {

    private final ManutencaoEquipamentoService manutencaoEquipamentoService;

    @PostMapping
    public ResponseEntity<ManutencaoEquipamentoResponse> salvar(@Valid @RequestBody ManutencaoEquipamentoRequest request,
                                                                Authentication authentication) {
        return ResponseEntity.ok(manutencaoEquipamentoService.salvar(request, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}")
    public ResponseEntity<Page<ManutencaoEquipamentoResponse>> buscarPorFazenda(
            @PathVariable Long fazendaId,
            @RequestParam(required = false) Long equipamentoId,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            Pageable pageable,
            Authentication authentication) {
        return ResponseEntity.ok(manutencaoEquipamentoService.buscarPorFazenda(fazendaId, equipamentoId, dataInicio, dataFim, pageable, authentication.getName()));
    }

    @GetMapping("/equipamento/{equipamentoId}")
    public ResponseEntity<Page<ManutencaoEquipamentoResponse>> buscarPorEquipamento(@PathVariable Long equipamentoId, Pageable pageable,
                                                                                    Authentication authentication) {
        return ResponseEntity.ok(manutencaoEquipamentoService.buscarPorEquipamento(equipamentoId, pageable, authentication.getName()));
    }

    @GetMapping("/fornecedor/{fornecedorId}/pendentes")
    public ResponseEntity<List<ManutencaoEquipamentoResponse>> listarPendentesPorFornecedor(@PathVariable Long fornecedorId,
                                                                                            Authentication authentication) {
        return ResponseEntity.ok(manutencaoEquipamentoService.listarPendentesPorFornecedor(fornecedorId, authentication.getName()));
    }

    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<List<ManutencaoEquipamentoResponse>> listarPorFornecedorEStatus(
            @PathVariable Long fornecedorId,
            @RequestParam StatusParcela status,
            Authentication authentication) {
        return ResponseEntity.ok(manutencaoEquipamentoService.listarPorFornecedorEStatus(fornecedorId, status, authentication.getName()));
    }

    @PatchMapping("/{id}/baixa")
    public ResponseEntity<ManutencaoEquipamentoResponse> darBaixa(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataBaixa,
            Authentication authentication) {
        return ResponseEntity.ok(manutencaoEquipamentoService.darBaixa(id, dataBaixa, authentication.getName()));
    }
}