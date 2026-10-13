package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.equipamento.ManutencaoEquipamentoRequest;
import com.example.fazendalosardo.dto.equipamento.ManutencaoEquipamentoResponse;
import com.example.fazendalosardo.service.ManutencaoEquipamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manutencao-equipamento")
@RequiredArgsConstructor
public class ManutencaoEquipamentoController {

    private final ManutencaoEquipamentoService manutencaoEquipamentoService;

    @PostMapping
    public ResponseEntity<ManutencaoEquipamentoResponse> salvar(@Valid @RequestBody ManutencaoEquipamentoRequest request,
                                                                Authentication authentication) {
        return ResponseEntity.ok(manutencaoEquipamentoService.salvar(request, authentication.getName()));
    }

    @GetMapping("/equipamento/{equipamentoId}")
    public ResponseEntity<Page<ManutencaoEquipamentoResponse>> buscarPorEquipamento(@PathVariable Long equipamentoId, Pageable pageable,
                                                                                    Authentication authentication) {
        return ResponseEntity.ok(manutencaoEquipamentoService.buscarPorEquipamento(equipamentoId, pageable, authentication.getName()));
    }
}