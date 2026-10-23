package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.animais.IngredienteRequest;
import com.example.fazendalosardo.dto.animais.IngredienteResponse;
import com.example.fazendalosardo.service.IngredienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ingrediente")
@RequiredArgsConstructor
public class IngredienteController {

    private final IngredienteService ingredienteService;

    @PostMapping
    public IngredienteResponse salvar(@RequestBody @Valid IngredienteRequest request, Authentication authentication) {
        return ingredienteService.salvar(request, authentication.getName());
    }

    @PutMapping("/{id}")
    public IngredienteResponse atualizar(@PathVariable Long id, @RequestBody @Valid IngredienteRequest request, Authentication authentication) {
        return ingredienteService.atualizar(id, request, authentication.getName());
    }

    @GetMapping("/fazenda/{fazendaId}")
    public List<IngredienteResponse> listarPorFazenda(@PathVariable Long fazendaId, Authentication authentication) {
        return ingredienteService.listarPorFazenda(fazendaId, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public void inativar(@PathVariable Long id, Authentication authentication) {
        ingredienteService.inativar(id, authentication.getName());
    }
}