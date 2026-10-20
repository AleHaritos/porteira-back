package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.equipamento.FornecedorPagamentoRequest;
import com.example.fazendalosardo.dto.equipamento.FornecedorRequest;
import com.example.fazendalosardo.dto.equipamento.FornecedorResponse;
import com.example.fazendalosardo.service.FornecedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public ResponseEntity<Page<FornecedorResponse>> listarPorFazenda(
            @PathVariable Long fazendaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        return ResponseEntity.ok(
                fornecedorService.listarPorFazenda(fazendaId, PageRequest.of(page, size), authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}/todos")
    public ResponseEntity<List<FornecedorResponse>> listarTodosPorFazenda(
            @PathVariable Long fazendaId, Authentication authentication) {
        return ResponseEntity.ok(fornecedorService.listarTodosPorFazenda(fazendaId, authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponse> buscarPorId(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(fornecedorService.buscarPorId(id, authentication.getName()));
    }

    @PostMapping("/{id}/pagamento")
    public ResponseEntity<FornecedorResponse> registrarPagamento(@PathVariable Long id, @Valid @RequestBody FornecedorPagamentoRequest request,
                                                                 Authentication authentication) {
        return ResponseEntity.ok(fornecedorService.registrarPagamento(id, request, authentication.getName()));
    }
}