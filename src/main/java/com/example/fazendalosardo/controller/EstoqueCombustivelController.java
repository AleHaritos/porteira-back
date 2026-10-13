package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.veiculos.EstoqueCombustivelResponse;
import com.example.fazendalosardo.dto.veiculos.ReabastecimentoEstoqueRequest;
import com.example.fazendalosardo.service.EstoqueCombustivelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoque-combustivel")
@RequiredArgsConstructor
public class EstoqueCombustivelController {

    private final EstoqueCombustivelService estoqueCombustivelService;

    @PostMapping("/reabastecer")
    public ResponseEntity<EstoqueCombustivelResponse> reabastecer(@Valid @RequestBody ReabastecimentoEstoqueRequest request,
                                                                  Authentication authentication) {
        return ResponseEntity.ok(estoqueCombustivelService.reabastecer(request, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}")
    public ResponseEntity<List<EstoqueCombustivelResponse>> listarPorFazenda(@PathVariable Long fazendaId,
                                                                             Authentication authentication) {
        return ResponseEntity.ok(estoqueCombustivelService.listarPorFazenda(fazendaId, authentication.getName()));
    }
}