package com.example.fazendalosardo.service;

import com.example.fazendalosardo.dto.UsuarioRequest;
import com.example.fazendalosardo.dto.UsuarioResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UsuarioService {

    UsuarioResponse salvarUsuario(UsuarioRequest request, String numeroAdminLogado);

    void desativarUsuario(Long usuarioId, String numeroAdminLogado);

    void atualizarSenha(String numero, String senha);

    Boolean verificacaoUsuario(String numero);

    UsuarioResponse buscarPorNumero(String numero);

    Page<UsuarioResponse> buscarUsuariosCadastradosPor(String numeroAdminLogado, Pageable pageable);

    void reativarUsuario(Long usuarioId, String numeroAdminLogado);

    List<UsuarioResponse> listarTodosMeusCadastros(String numeroUsuarioLogado);
}
