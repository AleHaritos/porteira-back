package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.animais.ConsultaLoteRequest;
import com.example.fazendalosardo.dto.animais.ConsultaLoteResponse;
import com.example.fazendalosardo.service.ConsultaLoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consulta-lote")
@RequiredArgsConstructor
public class ConsultaLoteController {

    private final ConsultaLoteService consultaLoteService;

    @PostMapping
    public ResponseEntity<ConsultaLoteResponse> salvar(@Valid @RequestBody ConsultaLoteRequest request,
                                                       Authentication authentication) {
        return ResponseEntity.ok(consultaLoteService.salvar(request, authentication.getName()));
    }

    @GetMapping("/lote/{loteId}")
    public ResponseEntity<Page<ConsultaLoteResponse>> buscarPorLote(
            @PathVariable Long loteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        return ResponseEntity.ok(consultaLoteService.buscarPorLote(loteId, page, size, authentication.getName()));
    }
}