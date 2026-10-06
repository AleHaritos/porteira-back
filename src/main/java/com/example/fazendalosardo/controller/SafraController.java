package com.example.fazendalosardo.controller;


import com.example.fazendalosardo.dto.safraDTO.SafraRequest;
import com.example.fazendalosardo.dto.safraDTO.SafraResponse;
import com.example.fazendalosardo.service.SafraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/safra")
public class SafraController {

    private final SafraService safraService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SafraResponse salvar(@RequestBody @Valid SafraRequest request) {
        return safraService.salvar(request);
    }

    @GetMapping("/{fazendaId}")
    public Page<SafraResponse> buscarPorFazenda(
            @PathVariable Long fazendaId,
            @RequestParam(required = false) LocalDate dataInicioDe,
            @RequestParam(required = false) LocalDate dataInicioAte,
            @PageableDefault(size = 10, sort = "dataInicio") Pageable pageable) {
        return safraService.buscarPorFazenda(fazendaId, dataInicioDe, dataInicioAte, pageable);
    }
}
