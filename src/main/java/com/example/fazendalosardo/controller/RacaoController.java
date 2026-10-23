package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.animais.RacaoRequest;
import com.example.fazendalosardo.dto.animais.RacaoResponse;
import com.example.fazendalosardo.service.RacaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/racao")
@RequiredArgsConstructor
public class RacaoController {

    private final RacaoService racaoService;

    @PostMapping
    public RacaoResponse salvar(@RequestBody @Valid RacaoRequest request, Authentication authentication) {
        return racaoService.salvar(request, authentication.getName());
    }

    @PutMapping("/{id}")
    public RacaoResponse atualizar(@PathVariable Long id, @RequestBody @Valid RacaoRequest request, Authentication authentication) {
        return racaoService.atualizar(id, request, authentication.getName());
    }

    @GetMapping("/fazenda/{fazendaId}")
    public List<RacaoResponse> listarPorFazenda(@PathVariable Long fazendaId, Authentication authentication) {
        return racaoService.listarPorFazenda(fazendaId, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public void inativar(@PathVariable Long id, Authentication authentication) {
        racaoService.inativar(id, authentication.getName());
    }
}