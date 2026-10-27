package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.veiculos.PostoRequest;
import com.example.fazendalosardo.dto.veiculos.PostoResponse;
import com.example.fazendalosardo.service.PostoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posto")
@RequiredArgsConstructor
public class PostoController {

    private final PostoService postoService;

    @PostMapping
    public ResponseEntity<PostoResponse> salvar(@Valid @RequestBody PostoRequest request, Authentication authentication) {
        return ResponseEntity.ok(postoService.salvar(request, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}")
    public ResponseEntity<Page<PostoResponse>> listarPorFazenda(@PathVariable Long fazendaId, Pageable pageable,
                                                                Authentication authentication) {
        return ResponseEntity.ok(postoService.listarPorFazenda(fazendaId, pageable, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}/todos")
    public ResponseEntity<List<PostoResponse>> listarTodosPorFazenda(@PathVariable Long fazendaId, Authentication authentication) {
        return ResponseEntity.ok(postoService.listarTodosPorFazenda(fazendaId, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, Authentication authentication) {
        postoService.excluir(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}