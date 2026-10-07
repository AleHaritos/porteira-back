package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.safraDTO.TalhaoRequest;
import com.example.fazendalosardo.dto.safraDTO.TalhaoResponse;
import com.example.fazendalosardo.service.TalhaoService;
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
@RequestMapping("/talhao")
@RequiredArgsConstructor
public class TalhaoController {

    private final TalhaoService talhaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TalhaoResponse salvar(@RequestBody @Valid TalhaoRequest request) {
        return talhaoService.salvar(request);
    }

    @GetMapping("/safra/{safraId}")
    public Page<TalhaoResponse> buscarPorSafra(
            @PathVariable Long safraId,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return talhaoService.buscarPorSafra(safraId, pageable);
    }

    @GetMapping("/safra/{safraId}/todos")
    public List<TalhaoResponse> listarPorSafra(@PathVariable Long safraId) {
        return talhaoService.listarPorSafra(safraId);
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        talhaoService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Long id) {
        talhaoService.reativar(id);
        return ResponseEntity.noContent().build();
    }
}