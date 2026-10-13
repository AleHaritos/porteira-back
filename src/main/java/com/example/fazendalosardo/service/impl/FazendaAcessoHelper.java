package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Usuario;
import com.example.fazendalosardo.repository.FazendaColaboradorRepository;
import com.example.fazendalosardo.repository.FazendaRepository;
import com.example.fazendalosardo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FazendaAcessoHelper {

    private final FazendaRepository fazendaRepository;
    private final UsuarioRepository usuarioRepository;
    private final FazendaColaboradorRepository fazendaColaboradorRepository;

    public Fazenda buscarComAcesso(Long fazendaId, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaRepository.findById(fazendaId)
                .orElseThrow(() -> new NotFoundException("Fazenda não encontrada"));
        validarAcesso(fazenda, numeroUsuarioLogado);
        return fazenda;
    }

    public void validarAcesso(Fazenda fazenda, String numeroUsuarioLogado) {
        Usuario usuario = usuarioRepository.findByNumero(numeroUsuarioLogado)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        boolean temAcesso = fazendaColaboradorRepository
                .existsByFazendaIdAndColaboradorId(fazenda.getId(), usuario.getId());

        if (!temAcesso) {
            throw new BusinessException("Você não tem acesso a essa fazenda");
        }
    }
}
