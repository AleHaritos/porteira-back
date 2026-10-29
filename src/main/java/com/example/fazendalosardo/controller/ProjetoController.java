package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.projeto.ProjetoRequest;
import com.example.fazendalosardo.dto.projeto.ProjetoResponse;
import com.example.fazendalosardo.dto.projeto.ProjetoUpdateRequest;
import com.example.fazendalosardo.service.ProjetoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projeto")
@RequiredArgsConstructor
public class ProjetoController {

    private final ProjetoService projetoService;

    @PostMapping
    public ResponseEntity<ProjetoResponse> salvar(@RequestBody @Valid ProjetoRequest request,
                                                  Authentication authentication) {
        return ResponseEntity.ok(projetoService.salvar(request, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}")
    public ResponseEntity<Page<ProjetoResponse>> buscarPorFazenda(
            @PathVariable Long fazendaId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(projetoService.buscarPorFazenda(fazendaId, PageRequest.of(page, size)));
    }

    @GetMapping("/fazenda/{fazendaId}/todos")
    public ResponseEntity<List<ProjetoResponse>> listarTodosPorFazenda(@PathVariable Long fazendaId) {
        return ResponseEntity.ok(projetoService.listarTodosPorFazenda(fazendaId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjetoResponse> atualizar(@PathVariable Long id,
                                                     @RequestBody @Valid ProjetoUpdateRequest request,
                                                     Authentication authentication) {
        return ResponseEntity.ok(projetoService.atualizar(id, request, authentication.getName()));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id, Authentication authentication) {
        projetoService.desativar(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Long id, Authentication authentication) {
        projetoService.reativar(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}