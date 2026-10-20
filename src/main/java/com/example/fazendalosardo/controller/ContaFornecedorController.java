package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.equipamento.ContaFornecedorRequest;
import com.example.fazendalosardo.dto.equipamento.ContaFornecedorResponse;
import com.example.fazendalosardo.service.ContaFornecedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/conta-fornecedor")
@RequiredArgsConstructor
public class ContaFornecedorController {

    private final ContaFornecedorService contaFornecedorService;

    @PostMapping
    public ResponseEntity<ContaFornecedorResponse> salvar(@Valid @RequestBody ContaFornecedorRequest request,
                                                          Authentication authentication) {
        return ResponseEntity.ok(contaFornecedorService.salvar(request, authentication.getName()));
    }

    @GetMapping("/fornecedor/{fornecedorId}/pendentes")
    public ResponseEntity<List<ContaFornecedorResponse>> listarPendentesPorFornecedor(@PathVariable Long fornecedorId,
                                                                                      Authentication authentication) {
        return ResponseEntity.ok(contaFornecedorService.listarPendentesPorFornecedor(fornecedorId, authentication.getName()));
    }

    @PatchMapping("/{id}/baixa")
    public ResponseEntity<ContaFornecedorResponse> darBaixa(
            @PathVariable Long id,
            @RequestParam(required = false) Long negocioId,
            @RequestParam(required = false) Long safraId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataBaixa,
            Authentication authentication) {
        return ResponseEntity.ok(contaFornecedorService.darBaixa(id, negocioId, safraId, dataBaixa, authentication.getName()));
    }
}