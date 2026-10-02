package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.UsuarioRequest;
import com.example.fazendalosardo.dto.UsuarioResponse;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.mapper.UsuarioMapper;
import com.example.fazendalosardo.model.Usuario;
import com.example.fazendalosardo.repository.UsuarioRepository;
import com.example.fazendalosardo.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioMapper usuarioMapper;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UsuarioResponse salvarUsuario(UsuarioRequest request, String numeroAdminLogado) {
        Usuario admin = usuarioRepository.findByNumero(numeroAdminLogado)
                .orElseThrow(() -> new NotFoundException("Usuário logado não encontrado"));

        if (!Boolean.TRUE.equals(admin.getAdmin())) {
            throw new BusinessException("Apenas administradores podem cadastrar usuários");
        }

        Usuario novoUsuario = usuarioMapper.toEntity(request);
        novoUsuario.setCadastradoPor(admin);

        return usuarioMapper.toResponse(usuarioRepository.save(novoUsuario));
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

    @Override
    @Transactional
    public void desativarUsuario(Long usuarioId, String numeroAdminLogado) {

        Usuario alvo = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (alvo.getCadastradoPor() == null || !alvo.getCadastradoPor().getNumero().equals(numeroAdminLogado)) {
            throw new BusinessException("Apenas quem cadastrou esse usuário pode desativá-lo");
        }

        alvo.setAtivo(false);
    }

    @Override
    @Transactional
    public void reativarUsuario(Long usuarioId, String numeroAdminLogado) {

        Usuario alvo = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        if (alvo.getCadastradoPor() == null || !alvo.getCadastradoPor().getNumero().equals(numeroAdminLogado)) {
            throw new BusinessException("Apenas quem cadastrou esse usuário pode reativá-lo");
        }

        alvo.setAtivo(true);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UsuarioResponse> buscarUsuariosCadastradosPor(String numeroAdminLogado, Pageable pageable) {

        Usuario admin = usuarioRepository.findByNumero(numeroAdminLogado)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        return usuarioRepository
                .findByCadastradoPorId(admin.getId(), pageable)
                .map(usuarioMapper::toResponse);
    }

}
