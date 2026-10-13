package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.equipamento.EquipamentoRequest;
import com.example.fazendalosardo.dto.equipamento.EquipamentoResponse;
import com.example.fazendalosardo.service.EquipamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/equipamento")
@RequiredArgsConstructor
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    @PostMapping
    public ResponseEntity<EquipamentoResponse> salvar(@Valid @RequestBody EquipamentoRequest request, Authentication authentication) {
        return ResponseEntity.ok(equipamentoService.salvar(request, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipamentoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody EquipamentoRequest request,
                                                         Authentication authentication) {
        return ResponseEntity.ok(equipamentoService.atualizar(id, request, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}")
    public ResponseEntity<Page<EquipamentoResponse>> buscarPorFazenda(@PathVariable Long fazendaId, Pageable pageable,
                                                                      Authentication authentication) {
        return ResponseEntity.ok(equipamentoService.buscarPorFazenda(fazendaId, pageable, authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoResponse> buscarPorId(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(equipamentoService.buscarPorId(id, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id, Authentication authentication) {
        equipamentoService.inativar(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}