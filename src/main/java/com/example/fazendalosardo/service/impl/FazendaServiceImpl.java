package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.fazendaDTO.FazendaRequest;
import com.example.fazendalosardo.dto.fazendaDTO.FazendaResponse;
import com.example.fazendalosardo.dto.fazendaDTO.FazendaResumoResponse;
import com.example.fazendalosardo.dto.UsuarioDTO;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.mapper.FazendaMapper;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.FazendaColaborador;
import com.example.fazendalosardo.model.Usuario;
import com.example.fazendalosardo.repository.FazendaColaboradorRepository;
import com.example.fazendalosardo.repository.FazendaRepository;
import com.example.fazendalosardo.repository.UsuarioRepository;
import com.example.fazendalosardo.service.FazendaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FazendaServiceImpl implements FazendaService {

    private final FazendaMapper fazendaMapper;
    private final FazendaRepository fazendaRepository;
    private final UsuarioRepository usuarioRepository;
    private final FazendaColaboradorRepository fazendaColaboradorRepository;

    @Override
    @Transactional
    public FazendaResponse criar(FazendaRequest request, String numeroUsuarioLogado) {
        Usuario dono = usuarioRepository.findByNumero(numeroUsuarioLogado)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        Fazenda fazenda = fazendaMapper.toEntity(request);
        fazenda.setDono(dono);
        Fazenda salva = fazendaRepository.save(fazenda);

        FazendaColaborador fazendaColaborador = new FazendaColaborador(null, salva, dono, LocalDateTime.now());
        fazendaColaboradorRepository.save(fazendaColaborador);

        List<UsuarioDTO> colaboradores = List.of(fazendaMapper.toResumo(dono));

        return fazendaMapper.toResponse(salva, colaboradores);
    }

    @Override
    @Transactional
    public void adicionarColaborador(Long fazendaId, String numeroColaborador, String numeroUsuarioLogado) {

        Fazenda fazenda = fazendaRepository.findById(fazendaId)
                .orElseThrow(() -> new NotFoundException("Fazenda não encontrada"));

        //Validacao do dono
        if (!fazenda.getDono().getNumero().equals(numeroUsuarioLogado)) {
            throw new BusinessException("Apenas o dono da fazenda pode adicionar colaboradores");
        }

        //Verificando colaborador
        Usuario colaborador = usuarioRepository.findByNumero(numeroColaborador)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com esse número"));

        //Verifica se colaborador já existe
        boolean jaEhColaborador = fazendaColaboradorRepository
                .existsByFazendaIdAndColaboradorId(fazendaId, colaborador.getId());

        if (jaEhColaborador) {
            throw new BusinessException("Esse usuário já é colaborador dessa fazenda");
        }

        FazendaColaborador novaParticipacao = new FazendaColaborador(
                null,
                fazenda,
                colaborador,
                LocalDateTime.now()
        );

        fazendaColaboradorRepository.save(novaParticipacao);
    }

    @Override
    @Transactional(readOnly = true)
    public FazendaResponse buscarPorId(Long id) {
        Fazenda fazenda = fazendaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Fazenda não encontrada"));

        List<UsuarioDTO> colaboradores = fazendaColaboradorRepository
                .findByFazendaId(id)
                .stream()
                .map(FazendaColaborador::getColaborador)
                .map(fazendaMapper::toResumo)
                .toList();

        return fazendaMapper.toResponse(fazenda, colaboradores);
    }

    // service/impl/FazendaServiceImpl.java
    @Override
    @Transactional(readOnly = true)
    public Page<FazendaResumoResponse> buscarFazendasPorColaborador(String numero, Pageable pageable) {

        Usuario usuario = usuarioRepository.findByNumero(numero)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        return fazendaRepository
                .buscarPorColaborador(usuario.getId(), pageable)
                .map(fazendaMapper::toResumoResponse);   // Page também tem .map(), converte item por item
    }

    @Override
    @Transactional
    public void removerColaborador(Long fazendaId, Long usuarioId) {
        Fazenda fazenda = fazendaRepository.findById(fazendaId)
                .orElseThrow(() -> new NotFoundException("Fazenda não encontrada"));

        if (fazenda.getDono().getId().equals(usuarioId)) {
            throw new BusinessException("O dono da fazenda não pode ser removido dos colaboradores");
        }

        boolean existe = fazendaColaboradorRepository.existsByFazendaIdAndColaboradorId(fazendaId, usuarioId);
        if (!existe) {
            throw new NotFoundException("Colaborador não encontrado nessa fazenda");
        }

        fazendaColaboradorRepository.deleteByFazendaIdAndColaboradorId(fazendaId, usuarioId);
    }

}
