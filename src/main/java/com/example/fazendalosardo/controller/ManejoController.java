package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.safraDTO.ManejoRequest;
import com.example.fazendalosardo.dto.safraDTO.ManejoResponse;
import com.example.fazendalosardo.dto.safraDTO.ManejoUpdateRequest;
import com.example.fazendalosardo.service.ManejoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manejo")
@RequiredArgsConstructor
public class ManejoController {

    private final ManejoService manejoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ManejoResponse salvar(@RequestBody @Valid ManejoRequest request) {
        return manejoService.salvar(request);
    }

    @GetMapping("/talhao/{talhaoId}")
    public Page<ManejoResponse> buscarPorTalhao(
            @PathVariable Long talhaoId,
            @PageableDefault(size = 10, sort = "data") Pageable pageable) {
        return manejoService.buscarPorTalhao(talhaoId, pageable);
    }

    @GetMapping("/talhao/{talhaoId}/todos")
    public List<ManejoResponse> listarPorTalhao(@PathVariable Long talhaoId) {
        return manejoService.listarPorTalhao(talhaoId);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ManejoResponse> atualizar(
            @PathVariable Long id,
            @RequestBody @Valid ManejoUpdateRequest request
    ) {
        return ResponseEntity.ok(manejoService.atualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        manejoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}