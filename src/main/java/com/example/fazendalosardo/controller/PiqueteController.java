package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.animais.PiqueteRequest;
import com.example.fazendalosardo.dto.animais.PiqueteResponse;
import com.example.fazendalosardo.service.PiqueteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/piquete")
@RequiredArgsConstructor
public class PiqueteController {

    private final PiqueteService piqueteService;

    @PostMapping
    public ResponseEntity<PiqueteResponse> salvar(@Valid @RequestBody PiqueteRequest request, Authentication authentication) {
        return ResponseEntity.ok(piqueteService.salvar(request, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PiqueteResponse> atualizar(@PathVariable Long id, @Valid @RequestBody PiqueteRequest request,
                                                     Authentication authentication) {
        return ResponseEntity.ok(piqueteService.atualizar(id, request, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}")
    public ResponseEntity<List<PiqueteResponse>> listarPorFazenda(@PathVariable Long fazendaId, Authentication authentication) {
        return ResponseEntity.ok(piqueteService.listarPorFazenda(fazendaId, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id, Authentication authentication) {
        piqueteService.inativar(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}