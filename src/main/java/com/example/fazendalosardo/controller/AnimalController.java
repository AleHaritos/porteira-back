package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.animais.*;
import com.example.fazendalosardo.model.enums.TipoAnimal;
import com.example.fazendalosardo.service.AnimalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/animal")
@RequiredArgsConstructor
public class AnimalController {

    private final AnimalService animalService;

    @PostMapping("/em-massa")
    public ResponseEntity<List<AnimalResponse>> salvarEmMassa(@Valid @RequestBody AnimalEmMassaRequest request,
                                                              Authentication authentication) {
        return ResponseEntity.ok(animalService.salvarEmMassa(request, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalResponse> atualizar(@PathVariable Long id, @Valid @RequestBody AnimalRequest request,
                                                    Authentication authentication) {
        return ResponseEntity.ok(animalService.atualizar(id, request, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}")
    public ResponseEntity<Page<AnimalResponse>> listarPorFazenda(
            @PathVariable Long fazendaId,
            @RequestParam(required = false) Long piqueteId,
            @RequestParam(required = false) Long loteId,
            @RequestParam(required = false) TipoAnimal tipo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        Page<AnimalResponse> resultado = animalService.listarPorFazenda(
                fazendaId, piqueteId, loteId, tipo, PageRequest.of(page, size), authentication.getName());
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/transferir")
    public void transferir(@RequestBody @Valid TransferenciaAnimalRequest request, Authentication authentication) {
        animalService.transferirAnimais(request, authentication.getName());
    }

    @PostMapping
    public ResponseEntity<AnimalResponse> salvar(@Valid @RequestBody AnimalRequest request,
                                                 Authentication authentication) {
        return ResponseEntity.ok(animalService.salvar(request, authentication.getName()));
    }

    @PostMapping("/vender")
    public OperacaoVendaResponse vender(@RequestBody @Valid VendaAnimalRequest request, Authentication authentication) {
        return animalService.venderAnimais(request, authentication.getName());
    }

    @GetMapping("/vendas/fazenda/{fazendaId}/periodo")
    public Page<OperacaoVendaResponse> listarOperacoesVendaPorPeriodo(
            @PathVariable Long fazendaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {
        return animalService.listarOperacoesVenda(fazendaId, dataInicio, dataFim, page, size, authentication.getName());
    }

    @GetMapping("/vendas/fazenda/{fazendaId}")
    public List<OperacaoVendaResponse> listarOperacoesVenda(@PathVariable Long fazendaId, Authentication authentication) {
        return animalService.listarOperacoesVenda(fazendaId, authentication.getName());
    }

    @GetMapping("/{id}/custo")
    public AnimalCustoResponse calcularCusto(@PathVariable Long id, Authentication authentication) {
        return animalService.calcularCusto(id, authentication.getName());
    }

    @GetMapping("/{id}/historico")
    public List<HistoricoCuidadoAnimalResponse> listarHistorico(@PathVariable Long id, Authentication authentication) {
        return animalService.listarHistorico(id, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, Authentication authentication) {
        animalService.excluir(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}