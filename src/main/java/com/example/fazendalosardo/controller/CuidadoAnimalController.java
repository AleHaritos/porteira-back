package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.animais.CuidadoAnimalRequest;
import com.example.fazendalosardo.dto.animais.CuidadoAnimalResponse;
import com.example.fazendalosardo.model.enums.TipoCuidado;
import com.example.fazendalosardo.service.CuidadoAnimalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/cuidado-animal")
@RequiredArgsConstructor
public class CuidadoAnimalController {

    private final CuidadoAnimalService cuidadoAnimalService;

    @PostMapping
    public CuidadoAnimalResponse salvar(@RequestBody @Valid CuidadoAnimalRequest request, Authentication authentication) {
        return cuidadoAnimalService.salvar(request, authentication.getName());
    }

    @GetMapping("/piquete/{piqueteId}")
    public Page<CuidadoAnimalResponse> listarPorPiquete(@PathVariable Long piqueteId,
                                                        @RequestParam TipoCuidado tipo,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
                                                        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
                                                        @RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        Authentication authentication) {
        return cuidadoAnimalService.listarPorPiquete(piqueteId, tipo, dataInicio, dataFim, page, size, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id, Authentication authentication) {
        cuidadoAnimalService.excluir(id, authentication.getName());
    }
}