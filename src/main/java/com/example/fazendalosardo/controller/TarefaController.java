package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.tarefa.*;
import com.example.fazendalosardo.service.TarefaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefa")
@RequiredArgsConstructor
public class TarefaController {

    private final TarefaService tarefaService;

    @PostMapping
    public ResponseEntity<TarefaResponse> salvar(@Valid @RequestBody TarefaRequest request, Authentication authentication) {
        return ResponseEntity.ok(tarefaService.salvar(request, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarefaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody TarefaRequest request,
                                                    Authentication authentication) {
        return ResponseEntity.ok(tarefaService.atualizar(id, request, authentication.getName()));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TarefaResponse> atualizarStatus(@PathVariable Long id, @Valid @RequestBody TarefaStatusRequest request,
                                                          Authentication authentication) {
        return ResponseEntity.ok(tarefaService.atualizarStatus(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, Authentication authentication) {
        tarefaService.excluir(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/fazenda/{fazendaId}")
    public ResponseEntity<List<TarefaResponse>> buscarPorFazenda(@PathVariable Long fazendaId, Authentication authentication) {
        return ResponseEntity.ok(tarefaService.buscarPorFazenda(fazendaId, authentication.getName()));
    }

    @PostMapping("/{tarefaId}/subtarefa")
    public ResponseEntity<SubTarefaResponse> adicionarSubtarefa(@PathVariable Long tarefaId, @Valid @RequestBody SubTarefaRequest request,
                                                                Authentication authentication) {
        return ResponseEntity.ok(tarefaService.adicionarSubtarefa(tarefaId, request, authentication.getName()));
    }

    @PatchMapping("/subtarefa/{subtarefaId}/alternar")
    public ResponseEntity<SubTarefaResponse> alternarSubtarefa(@PathVariable Long subtarefaId, Authentication authentication) {
        return ResponseEntity.ok(tarefaService.alternarSubtarefa(subtarefaId, authentication.getName()));
    }

    @DeleteMapping("/subtarefa/{subtarefaId}")
    public ResponseEntity<Void> excluirSubtarefa(@PathVariable Long subtarefaId, Authentication authentication) {
        tarefaService.excluirSubtarefa(subtarefaId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}