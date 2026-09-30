package com.example.fazendalosardo.controller;


import com.example.fazendalosardo.dto.AdicionarColaboradorRequest;
import com.example.fazendalosardo.dto.FazendaRequest;
import com.example.fazendalosardo.dto.FazendaResponse;
import com.example.fazendalosardo.dto.FazendaResumoResponse;
import com.example.fazendalosardo.service.FazendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fazenda")
@RequiredArgsConstructor
public class FazendaController {

    private final FazendaService fazendaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FazendaResponse criar(@RequestBody @Valid FazendaRequest request, Authentication authentication) {
        return fazendaService.criar(request, authentication.getName());
    }


    @GetMapping("/{fazendaId}")
    @ResponseStatus(HttpStatus.OK)
    public FazendaResponse buscarFazendaPorId(@PathVariable(name = "fazendaId") Long fazendaId) {
        return fazendaService.buscarPorId(fazendaId);
    }

    @GetMapping("/listarFazendas")
    @ResponseStatus(HttpStatus.OK)
    public Page<FazendaResumoResponse> minhasFazendas(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "id") Pageable pageable) {
        return fazendaService.buscarFazendasPorColaborador(authentication.getName(), pageable);
    }

    @PostMapping("/{fazendaId}/colaboradores")
    @ResponseStatus(HttpStatus.CREATED)
    public void adicionarColaborador(
            @PathVariable Long fazendaId,
            @RequestBody @Valid AdicionarColaboradorRequest request,
            Authentication authentication) {

        fazendaService.adicionarColaborador(fazendaId, request.numero(), authentication.getName());
    }

    @DeleteMapping("/{fazendaId}/colaboradores/{usuarioId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removerColaborador(@PathVariable Long fazendaId, @PathVariable Long usuarioId) {
        fazendaService.removerColaborador(fazendaId, usuarioId);
    }

}
