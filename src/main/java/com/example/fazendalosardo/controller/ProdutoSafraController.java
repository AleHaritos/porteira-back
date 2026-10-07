package com.example.fazendalosardo.controller;


import com.example.fazendalosardo.dto.safraDTO.ProdutoSafraRequest;
import com.example.fazendalosardo.dto.safraDTO.ProdutoSafraResponse;
import com.example.fazendalosardo.service.ProdutoSafraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produto-safra")
@RequiredArgsConstructor
public class ProdutoSafraController {

    private final ProdutoSafraService produtoSafraService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoSafraResponse salvar(@RequestBody @Valid ProdutoSafraRequest request) {
        return produtoSafraService.salvar(request);
    }

    @GetMapping("/safra/{safraId}")
    public Page<ProdutoSafraResponse> buscarPorSafra(
            @PathVariable Long safraId,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return produtoSafraService.buscarPorSafra(safraId, pageable);
    }

    @GetMapping("/safra/{safraId}/todos")
    public List<ProdutoSafraResponse> listarPorSafra(@PathVariable Long safraId) {
        return produtoSafraService.listarPorSafra(safraId);
    }
}
