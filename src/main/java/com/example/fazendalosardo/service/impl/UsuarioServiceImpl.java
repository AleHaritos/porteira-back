package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.UsuarioRequest;
import com.example.fazendalosardo.dto.UsuarioResponse;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.mapper.UsuarioMapper;
import com.example.fazendalosardo.model.Usuario;
import com.example.fazendalosardo.repository.UsuarioRepository;
import com.example.fazendalosardo.service.UsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioResponse salvarUsuario(UsuarioRequest request) {
        Usuario usuario = usuarioMapper.toEntity(request);
        return usuarioMapper.toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional
    public void atualizarSenha(String numero, String novaSenha) {
        Usuario usuario = usuarioRepository.findByNumero(numero)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        usuario.setSenha(passwordEncoder.encode(novaSenha));

    }

    @Override
    public Boolean verificacaoUsuario(String numero) {
        Usuario usuario = usuarioRepository.findByNumero(numero)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        return usuario.getSenha() != null && !usuario.getSenha().isBlank();
    }

    @Override
    public UsuarioResponse buscarPorNumero(String numero) {
        Usuario usuario = usuarioRepository.findByNumero(numero)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));
        return usuarioMapper.toResponse(usuario);
    }
}
