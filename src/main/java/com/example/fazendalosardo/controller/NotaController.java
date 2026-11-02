package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.NotaRequest;
import com.example.fazendalosardo.dto.NotaResponse;
import com.example.fazendalosardo.service.NotaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/nota")
@RequiredArgsConstructor
public class NotaController {

    private final NotaService notaService;

    @PostMapping
    public ResponseEntity<NotaResponse> salvar(@Valid @RequestBody NotaRequest request, Authentication authentication) {
        return ResponseEntity.ok(notaService.salvar(request, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody NotaRequest request,
                                                  Authentication authentication) {
        return ResponseEntity.ok(notaService.atualizar(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, Authentication authentication) {
        notaService.excluir(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/fazenda/{fazendaId}")
    public ResponseEntity<List<NotaResponse>> buscarPorFazenda(@PathVariable Long fazendaId, Authentication authentication) {
        return ResponseEntity.ok(notaService.buscarPorFazenda(fazendaId, authentication.getName()));
    }
}