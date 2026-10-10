package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.financeiro.ContaPendenteRequest;
import com.example.fazendalosardo.dto.financeiro.ContaPendenteResponse;
import com.example.fazendalosardo.dto.financeiro.ParcelaContaPendenteResponse;
import com.example.fazendalosardo.model.enums.StatusParcela;
import com.example.fazendalosardo.model.enums.TipoTransacao;
import com.example.fazendalosardo.service.ContaPendenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/conta-pendente")
@RequiredArgsConstructor
public class ContaPendenteController {

    private final ContaPendenteService contaPendenteService;

    @PostMapping
    public ResponseEntity<ContaPendenteResponse> salvar(@RequestBody ContaPendenteRequest request,
                                                        Authentication authentication) {
        return ResponseEntity.ok(contaPendenteService.salvar(request, authentication.getName()));
    }

    @GetMapping("/fazenda/{fazendaId}/parcelas")
    public ResponseEntity<Page<ParcelaContaPendenteResponse>> buscarParcelas(
            @PathVariable Long fazendaId,
            @RequestParam(required = false) StatusParcela status,
            @RequestParam(required = false) TipoTransacao tipo,
            Pageable pageable,
            Authentication authentication) {
        return ResponseEntity.ok(contaPendenteService.buscarParcelasPorFazenda(fazendaId, status, tipo, pageable, authentication.getName()));
    }

    @PatchMapping("/parcela/{parcelaId}/baixa")
    public ResponseEntity<ParcelaContaPendenteResponse> darBaixa(
            @PathVariable Long parcelaId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataBaixa,
            Authentication authentication) {
        return ResponseEntity.ok(contaPendenteService.darBaixa(parcelaId, dataBaixa, authentication.getName()));
    }
}