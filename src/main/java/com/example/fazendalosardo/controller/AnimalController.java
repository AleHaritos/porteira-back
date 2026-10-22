package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.animais.AnimalEmMassaRequest;
import com.example.fazendalosardo.dto.animais.AnimalRequest;
import com.example.fazendalosardo.dto.animais.AnimalResponse;
import com.example.fazendalosardo.dto.animais.VendaAnimalRequest;
import com.example.fazendalosardo.model.enums.TipoAnimal;
import com.example.fazendalosardo.service.AnimalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/vender")
    public void vender(@RequestBody @Valid VendaAnimalRequest request, Authentication authentication) {
        animalService.venderAnimais(request, authentication.getName());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, Authentication authentication) {
        animalService.excluir(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}