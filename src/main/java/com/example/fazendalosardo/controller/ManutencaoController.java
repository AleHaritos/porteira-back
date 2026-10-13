package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.veiculos.ManutencaoRequest;
import com.example.fazendalosardo.dto.veiculos.ManutencaoResponse;
import com.example.fazendalosardo.service.ManutencaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manutencao")
@RequiredArgsConstructor
public class ManutencaoController {

    private final ManutencaoService manutencaoService;

    @PostMapping
    public ResponseEntity<ManutencaoResponse> salvar(@Valid @RequestBody ManutencaoRequest request,
                                                     Authentication authentication) {
        return ResponseEntity.ok(manutencaoService.salvar(request, authentication.getName()));
    }

    @GetMapping("/veiculo/{veiculoId}")
    public ResponseEntity<Page<ManutencaoResponse>> buscarPorVeiculo(@PathVariable Long veiculoId, Pageable pageable,
                                                                     Authentication authentication) {
        return ResponseEntity.ok(manutencaoService.buscarPorVeiculo(veiculoId, pageable, authentication.getName()));
    }
}