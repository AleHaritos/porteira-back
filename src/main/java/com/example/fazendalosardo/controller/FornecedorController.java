package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.equipamento.FornecedorPagamentoRequest;
import com.example.fazendalosardo.dto.equipamento.FornecedorRequest;
import com.example.fazendalosardo.dto.equipamento.FornecedorResponse;
import com.example.fazendalosardo.service.FornecedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fornecedor")
@RequiredArgsConstructor
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @PostMapping
    public ResponseEntity<FornecedorResponse> salvar(@Valid @RequestBody FornecedorRequest request, Authentication authentication) {
        return ResponseEntity.ok(fornecedorService.salvar(request, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}")
    public ResponseEntity<List<FornecedorResponse>> listarPorFazenda(@PathVariable Long fazendaId, Authentication authentication) {
        return ResponseEntity.ok(fornecedorService.listarPorFazenda(fazendaId, authentication.getName()));
    }

    @PostMapping("/{id}/pagamento")
    public ResponseEntity<FornecedorResponse> registrarPagamento(@PathVariable Long id, @Valid @RequestBody FornecedorPagamentoRequest request,
                                                                 Authentication authentication) {
        return ResponseEntity.ok(fornecedorService.registrarPagamento(id, request, authentication.getName()));
    }
}