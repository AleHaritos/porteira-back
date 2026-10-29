package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.safraDTO.ColheitaTalhaoRequest;
import com.example.fazendalosardo.dto.safraDTO.ColheitaTalhaoResponse;
import com.example.fazendalosardo.dto.safraDTO.ColheitaTalhaoUpdateRequest;
import com.example.fazendalosardo.service.ColheitaTalhaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colheita-talhao")
@RequiredArgsConstructor
public class ColheitaTalhaoController {

    private final ColheitaTalhaoService colheitaTalhaoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ColheitaTalhaoResponse salvar(@RequestBody @Valid ColheitaTalhaoRequest request) {
        return colheitaTalhaoService.salvar(request);
    }

    @PutMapping("/{id}")
    public ColheitaTalhaoResponse atualizar(@PathVariable Long id, @RequestBody @Valid ColheitaTalhaoUpdateRequest request) {
        return colheitaTalhaoService.atualizar(id, request);
    }

    @GetMapping("/talhao/{talhaoId}")
    public List<ColheitaTalhaoResponse> listarPorTalhao(@PathVariable Long talhaoId) {
        return colheitaTalhaoService.listarPorTalhao(talhaoId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        colheitaTalhaoService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}