package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.animais.ResumoAnimaisResponse;
import com.example.fazendalosardo.service.ResumoAnimaisService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/resumo-animais")
@RequiredArgsConstructor
public class ResumoAnimaisController {

    private final ResumoAnimaisService resumoAnimaisService;

    @GetMapping
    public ResumoAnimaisResponse buscar(@RequestParam Long fazendaId,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
                                        Authentication authentication) {
        return resumoAnimaisService.buscarResumo(fazendaId, dataInicio, dataFim, authentication.getName());
    }
}