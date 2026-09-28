package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.UsuarioRequest;
import com.example.fazendalosardo.dto.UsuarioResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface UsuarioService {

    UsuarioResponse salvarUsuario(UsuarioRequest usuario);

    void atualizarSenha(String numero, String senha);

    Boolean verificacaoUsuario(String numero);

    UsuarioResponse buscarPorNumero(String numero);
}
