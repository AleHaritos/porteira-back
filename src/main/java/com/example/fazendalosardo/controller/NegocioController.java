package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.negocio.NegocioRequest;
import com.example.fazendalosardo.dto.negocio.NegocioResponse;
import com.example.fazendalosardo.dto.negocio.NegocioUpdateRequest;
import com.example.fazendalosardo.service.NegocioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/negocio")
@RequiredArgsConstructor
public class NegocioController {

    private final NegocioService negocioService;

    @PostMapping
    public ResponseEntity<NegocioResponse> salvar(@RequestBody @Valid NegocioRequest request,
                                                  Authentication authentication) {
        return ResponseEntity.ok(negocioService.salvar(request, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}")
    public ResponseEntity<Page<NegocioResponse>> buscarPorFazenda(
            @PathVariable Long fazendaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(negocioService.buscarPorFazenda(fazendaId, PageRequest.of(page, size)));
    }

    @GetMapping("/fazenda/{fazendaId}/todos")
    public ResponseEntity<List<NegocioResponse>> listarTodosPorFazenda(@PathVariable Long fazendaId) {
        return ResponseEntity.ok(negocioService.listarTodosPorFazenda(fazendaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NegocioResponse> atualizar(@PathVariable Long id,
                                                     @RequestBody @Valid NegocioUpdateRequest request,
                                                     Authentication authentication) {
        return ResponseEntity.ok(negocioService.atualizar(id, request, authentication.getName()));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id, Authentication authentication) {
        negocioService.desativar(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Long id, Authentication authentication) {
        negocioService.reativar(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}