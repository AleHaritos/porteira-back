package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.veiculos.VeiculoRequest;
import com.example.fazendalosardo.dto.veiculos.VeiculoResponse;
import com.example.fazendalosardo.service.VeiculoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/veiculo")
@RequiredArgsConstructor
public class VeiculoController {

    private final VeiculoService veiculoService;

    @PostMapping
    public ResponseEntity<VeiculoResponse> salvar(@Valid @RequestBody VeiculoRequest request, Authentication authentication) {
        return ResponseEntity.ok(veiculoService.salvar(request, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeiculoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody VeiculoRequest request,
                                                     Authentication authentication) {
        return ResponseEntity.ok(veiculoService.atualizar(id, request, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}")
    public ResponseEntity<Page<VeiculoResponse>> buscarPorFazenda(@PathVariable Long fazendaId, Pageable pageable,
                                                                  Authentication authentication) {
        return ResponseEntity.ok(veiculoService.buscarPorFazenda(fazendaId, pageable, authentication.getName()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeiculoResponse> buscarPorId(@PathVariable Long id, Authentication authentication) {
        return ResponseEntity.ok(veiculoService.buscarPorId(id, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inativar(@PathVariable Long id, Authentication authentication) {
        veiculoService.inativar(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}