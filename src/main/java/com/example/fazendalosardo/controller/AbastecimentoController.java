package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.veiculos.AbastecimentoRequest;
import com.example.fazendalosardo.dto.veiculos.AbastecimentoResponse;
import com.example.fazendalosardo.service.AbastecimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/abastecimento")
@RequiredArgsConstructor
public class AbastecimentoController {

    private final AbastecimentoService abastecimentoService;

    @PostMapping
    public ResponseEntity<AbastecimentoResponse> salvar(@Valid @RequestBody AbastecimentoRequest request,
                                                        Authentication authentication) {
        return ResponseEntity.ok(abastecimentoService.salvar(request, authentication.getName()));
    }

    @GetMapping("/veiculo/{veiculoId}")
    public ResponseEntity<Page<AbastecimentoResponse>> buscarPorVeiculo(@PathVariable Long veiculoId, Pageable pageable,
                                                                        Authentication authentication) {
        return ResponseEntity.ok(abastecimentoService.buscarPorVeiculo(veiculoId, pageable, authentication.getName()));
    }
}