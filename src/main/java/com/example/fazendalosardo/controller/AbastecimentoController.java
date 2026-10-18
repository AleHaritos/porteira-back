package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.veiculos.AbastecimentoRequest;
import com.example.fazendalosardo.dto.veiculos.AbastecimentoResponse;
import com.example.fazendalosardo.dto.veiculos.AbastecimentoResumoResponse;
import com.example.fazendalosardo.service.AbastecimentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/abastecimento")
@RequiredArgsConstructor
public class AbastecimentoController {

    private final AbastecimentoService abastecimentoService;

    @PostMapping
    public ResponseEntity<AbastecimentoResponse> salvar(@Valid @RequestBody AbastecimentoRequest request,
                                                        Authentication authentication) {
        return ResponseEntity.ok(abastecimentoService.salvar(request, authentication.getName()));
    }

    @GetMapping("/veiculo/{veiculoId}")
    public ResponseEntity<Page<AbastecimentoResponse>> buscarPorVeiculo(@PathVariable Long veiculoId, Pageable pageable,
                                                                        Authentication authentication) {
        return ResponseEntity.ok(abastecimentoService.buscarPorVeiculo(veiculoId, pageable, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}")
    public ResponseEntity<Page<AbastecimentoResponse>> buscarPorFazenda(@PathVariable Long fazendaId,
                                                                        @RequestParam(required = false) Long veiculoId,
                                                                        @RequestParam(required = false) LocalDate dataInicio,
                                                                        @RequestParam(required = false) LocalDate dataFim,
                                                                        @RequestParam(required = false) Long negocioId,
                                                                        @RequestParam(required = false) Long safraId,
                                                                        Pageable pageable,
                                                                        Authentication authentication) {
        return ResponseEntity.ok(abastecimentoService.buscarPorFazenda(fazendaId, veiculoId, dataInicio, dataFim, negocioId, safraId, pageable, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}/resumo")
    public ResponseEntity<AbastecimentoResumoResponse> buscarResumo(@PathVariable Long fazendaId,
                                                                    @RequestParam(required = false) Long veiculoId,
                                                                    @RequestParam(required = false) LocalDate dataInicio,
                                                                    @RequestParam(required = false) LocalDate dataFim,
                                                                    @RequestParam(required = false) Long negocioId,
                                                                    @RequestParam(required = false) Long safraId,
                                                                    Authentication authentication) {
        return ResponseEntity.ok(abastecimentoService.buscarResumo(fazendaId, veiculoId, dataInicio, dataFim, negocioId, safraId, authentication.getName()));
    }

    @GetMapping("/posto/{postoId}")
    public ResponseEntity<Page<AbastecimentoResponse>> buscarPorPosto(@PathVariable Long postoId, Pageable pageable,
                                                                      Authentication authentication) {
        return ResponseEntity.ok(abastecimentoService.buscarPorPosto(postoId, pageable, authentication.getName()));
    }

    @PatchMapping("/{id}/baixa")
    public ResponseEntity<AbastecimentoResponse> darBaixa(@PathVariable Long id,
                                                          @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataBaixa,
                                                          Authentication authentication) {
        return ResponseEntity.ok(abastecimentoService.darBaixa(id, dataBaixa, authentication.getName()));
    }
}