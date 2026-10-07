package com.example.fazendalosardo.controller;


import com.example.fazendalosardo.dto.SenhaRequest;
import com.example.fazendalosardo.dto.UsuarioRequest;
import com.example.fazendalosardo.dto.UsuarioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.example.fazendalosardo.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse salvarUsuario(@RequestBody @Valid UsuarioRequest request, Authentication authentication) {
        log.info("Salvando usuário...");
        return usuarioService.salvarUsuario(request, authentication.getName());
    }

    @GetMapping("/{numero}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean verificacaoUsuario(@PathVariable(value = "numero") String numero) {
        log.info("Buscando usuário: " + numero);
        return usuarioService.verificacaoUsuario(numero);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizarSenha(@RequestBody @Valid SenhaRequest request) {
        log.info("Atualizando senha e login");
        usuarioService.atualizarSenha(request.numero(), request.senha());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<UsuarioResponse> meusCadastros(
            Authentication authentication,
            @PageableDefault(size = 10, sort = "nome") Pageable pageable) {
        return usuarioService.buscarUsuariosCadastradosPor(authentication.getName(), pageable);
    }

    @PatchMapping("/{id}/desativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desativar(@PathVariable Long id, Authentication authentication) {
        usuarioService.desativarUsuario(id, authentication.getName());
    }

    @PatchMapping("/{id}/reativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reativar(@PathVariable Long id, Authentication authentication) {
        usuarioService.reativarUsuario(id, authentication.getName());
    }

    @GetMapping("/meus-cadastros/todos")
    public ResponseEntity<List<UsuarioResponse>> listarTodosMeusCadastros(Authentication authentication) {
        return ResponseEntity.ok(usuarioService.listarTodosMeusCadastros(authentication.getName()));
    }

}
