package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.financeiro.ResumoFinanceiroResponse;
import com.example.fazendalosardo.dto.financeiro.TransacaoRequest;
import com.example.fazendalosardo.dto.financeiro.TransacaoResponse;
import com.example.fazendalosardo.dto.financeiro.TransacaoUpdateRequest;
import com.example.fazendalosardo.model.enums.TipoTransacao;
import com.example.fazendalosardo.service.TransacaoService;
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
@RequestMapping("/transacao")
@RequiredArgsConstructor
public class TransacaoController {

    private final TransacaoService transacaoService;

    @PostMapping
    public ResponseEntity<TransacaoResponse> salvar(@RequestBody @Valid TransacaoRequest request,
                                                    Authentication authentication) {
        return ResponseEntity.ok(transacaoService.salvar(request, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}")
    public ResponseEntity<Page<TransacaoResponse>> buscarPorFazenda(
            @PathVariable Long fazendaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            @RequestParam(required = false) TipoTransacao tipo,
            Pageable pageable,
            Authentication authentication) {
        return ResponseEntity.ok(
                transacaoService.buscarPorFazenda(fazendaId, dataInicio, dataFim, tipo, pageable, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransacaoResponse> atualizar(@PathVariable Long id,
                                                       @RequestBody @Valid TransacaoUpdateRequest request,
                                                       Authentication authentication) {
        return ResponseEntity.ok(transacaoService.atualizar(id, request, authentication.getName()));
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id, Authentication authentication) {
        transacaoService.desativar(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/fazenda/{fazendaId}/resumo")
    public ResponseEntity<ResumoFinanceiroResponse> buscarResumo(
            @PathVariable Long fazendaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Authentication authentication) {
        return ResponseEntity.ok(transacaoService.buscarResumo(fazendaId, dataInicio, dataFim, authentication.getName()));
    }

    @GetMapping("/negocio/{negocioId}/resumo")
    public ResponseEntity<ResumoFinanceiroResponse> buscarResumoPorNegocio(
            @PathVariable Long negocioId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Authentication authentication) {
        String numeroUsuarioLogado = authentication.getName();
        return ResponseEntity.ok(transacaoService.buscarResumoPorNegocio(negocioId, dataInicio, dataFim, numeroUsuarioLogado));
    }

    @PatchMapping("/{id}/reativar")
    public ResponseEntity<Void> reativar(@PathVariable Long id, Authentication authentication) {
        transacaoService.reativar(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}