package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.veiculos.ManutencaoRequest;
import com.example.fazendalosardo.dto.veiculos.ManutencaoResponse;
import com.example.fazendalosardo.dto.veiculos.ManutencaoResumoResponse;
import com.example.fazendalosardo.model.enums.StatusParcela;
import com.example.fazendalosardo.service.ManutencaoService;
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
@RequestMapping("/manutencao")
@RequiredArgsConstructor
public class ManutencaoController {

    private final ManutencaoService manutencaoService;

    @PostMapping
    public ResponseEntity<ManutencaoResponse> salvar(@Valid @RequestBody ManutencaoRequest request,
                                                     Authentication authentication) {
        return ResponseEntity.ok(manutencaoService.salvar(request, authentication.getName()));
    }

    @GetMapping("/veiculo/{veiculoId}/periodo")
    public ResponseEntity<Page<ManutencaoResponse>> buscarPorVeiculoEPeriodo(
            @PathVariable Long veiculoId,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            Pageable pageable,
            Authentication authentication) {
        return ResponseEntity.ok(manutencaoService.buscarPorVeiculoEPeriodo(veiculoId, dataInicio, dataFim, pageable, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}")
    public ResponseEntity<Page<ManutencaoResponse>> buscarPorFazenda(
            @PathVariable Long fazendaId,
            @RequestParam(required = false) Long veiculoId,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            Pageable pageable,
            Authentication authentication) {
        return ResponseEntity.ok(manutencaoService.buscarPorFazenda(fazendaId, veiculoId, dataInicio, dataFim, pageable, authentication.getName()));
    }

    @GetMapping("/veiculo/{veiculoId}/resumo")
    public ResponseEntity<ManutencaoResumoResponse> buscarResumoPorVeiculo(
            @PathVariable Long veiculoId,
            @RequestParam(required = false) LocalDate dataInicio,
            @RequestParam(required = false) LocalDate dataFim,
            Authentication authentication) {
        return ResponseEntity.ok(manutencaoService.buscarResumoPorVeiculo(veiculoId, dataInicio, dataFim, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, Authentication authentication) {
        manutencaoService.excluir(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/veiculo/{veiculoId}")
    public ResponseEntity<Page<ManutencaoResponse>> buscarPorVeiculo(@PathVariable Long veiculoId, Pageable pageable,
                                                                     Authentication authentication) {
        return ResponseEntity.ok(manutencaoService.buscarPorVeiculo(veiculoId, pageable, authentication.getName()));
    }

    @GetMapping("/fornecedor/{fornecedorId}/pendentes")
    public ResponseEntity<List<ManutencaoResponse>> listarPendentesPorFornecedor(@PathVariable Long fornecedorId,
                                                                                 Authentication authentication) {
        return ResponseEntity.ok(manutencaoService.listarPendentesPorFornecedor(fornecedorId, authentication.getName()));
    }

    @PatchMapping("/{id}/baixa")
    public ResponseEntity<ManutencaoResponse> darBaixa(
            @PathVariable Long id,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataBaixa,
            Authentication authentication) {
        return ResponseEntity.ok(manutencaoService.darBaixa(id, dataBaixa, authentication.getName()));
    }

    @GetMapping("/fornecedor/{fornecedorId}")
    public ResponseEntity<List<ManutencaoResponse>> listarPorFornecedorEStatus(
            @PathVariable Long fornecedorId,
            @RequestParam StatusParcela status,
            Authentication authentication) {
        return ResponseEntity.ok(manutencaoService.listarPorFornecedorEStatus(fornecedorId, status, authentication.getName()));
    }
}