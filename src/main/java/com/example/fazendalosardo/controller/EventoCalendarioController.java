package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.calendario.EventoCalendarioRequest;
import com.example.fazendalosardo.dto.calendario.EventoCalendarioResponse;
import com.example.fazendalosardo.service.EventoCalendarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/evento-calendario")
@RequiredArgsConstructor
public class EventoCalendarioController {

    private final EventoCalendarioService eventoCalendarioService;

    @PostMapping
    public ResponseEntity<EventoCalendarioResponse> salvar(@Valid @RequestBody EventoCalendarioRequest request,
                                                           Authentication authentication) {
        return ResponseEntity.ok(eventoCalendarioService.salvar(request, authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventoCalendarioResponse> atualizar(@PathVariable Long id,
                                                              @Valid @RequestBody EventoCalendarioRequest request,
                                                              Authentication authentication) {
        return ResponseEntity.ok(eventoCalendarioService.atualizar(id, request, authentication.getName()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id, Authentication authentication) {
        eventoCalendarioService.excluir(id, authentication.getName());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/fazenda/{fazendaId}")
    public ResponseEntity<List<EventoCalendarioResponse>> buscarPorFazendaEPeriodo(
            @PathVariable Long fazendaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Authentication authentication) {
        return ResponseEntity.ok(eventoCalendarioService.buscarPorFazendaEPeriodo(fazendaId, dataInicio, dataFim, authentication.getName()));
    }
}