package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.veiculos.ManutencaoRequest;
import com.example.fazendalosardo.dto.veiculos.ManutencaoResponse;
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
}