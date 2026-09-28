package com.example.fazendalosardo.controller;


import com.example.fazendalosardo.dto.SenhaRequest;
import com.example.fazendalosardo.dto.UsuarioRequest;
import com.example.fazendalosardo.dto.UsuarioResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import com.example.fazendalosardo.service.UsuarioService;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
@Slf4j
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse salvarUsuario(@RequestBody @Valid UsuarioRequest request) {
        log.info("Salvando usuário...");
        return usuarioService.salvarUsuario(request);
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

}
