// controller/AuthController.java
package com.example.fazendalosardo.controller;

import com.example.fazendalosardo.dto.LoginRequest;
import com.example.fazendalosardo.dto.TokenResponse;
import com.example.fazendalosardo.dto.UsuarioResponse;
import com.example.fazendalosardo.service.AuthService;
import com.example.fazendalosardo.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/usuarioLogado")
    public UsuarioResponse resgatarUsuarioLogado(Authentication authentication) {
        return usuarioService.buscarPorNumero(authentication.getName());
    }
}