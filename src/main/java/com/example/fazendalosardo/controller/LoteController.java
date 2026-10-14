package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.animais.LoteRequest;
import com.example.fazendalosardo.dto.animais.LoteResponse;
import com.example.fazendalosardo.service.LoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lote")
@RequiredArgsConstructor
public class LoteController {

    private final LoteService loteService;

    @PostMapping
    public ResponseEntity<LoteResponse> salvar(@Valid @RequestBody LoteRequest request, Authentication authentication) {
        return ResponseEntity.ok(loteService.salvar(request, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<LoteResponse> atualizar(@PathVariable Long id, @Valid @RequestBody LoteRequest request,
                                                  Authentication authentication) {
        return ResponseEntity.ok(loteService.atualizar(id, request, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}")
    public ResponseEntity<List<LoteResponse>> listarPorFazenda(@PathVariable Long fazendaId, Authentication authentication) {
        return ResponseEntity.ok(loteService.listarPorFazenda(fazendaId, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id, Authentication authentication) {
        loteService.inativar(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}