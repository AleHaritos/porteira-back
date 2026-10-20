package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.safraDTO.CustoFixoTalhaoRequest;
import com.example.fazendalosardo.dto.safraDTO.CustoFixoTalhaoResponse;
import com.example.fazendalosardo.service.CustoFixoTalhaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/custo-fixo-talhao")
@RequiredArgsConstructor
public class CustoFixoTalhaoController {

    private final CustoFixoTalhaoService custoFixoTalhaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustoFixoTalhaoResponse salvar(@RequestBody @Valid CustoFixoTalhaoRequest request) {
        return custoFixoTalhaoService.salvar(request);
    }

    @GetMapping("/talhao/{talhaoId}")
    public List<CustoFixoTalhaoResponse> listarPorTalhao(@PathVariable Long talhaoId) {
        return custoFixoTalhaoService.listarPorTalhao(talhaoId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        custoFixoTalhaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}