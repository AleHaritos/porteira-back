package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.NotaRequest;
import com.example.fazendalosardo.dto.NotaResponse;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Negocio;
import com.example.fazendalosardo.model.Nota;
import com.example.fazendalosardo.model.Projeto;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.NegocioRepository;
import com.example.fazendalosardo.repository.NotaRepository;
import com.example.fazendalosardo.repository.ProjetoRepository;
import com.example.fazendalosardo.repository.safras.SafraRepository;
import com.example.fazendalosardo.service.NotaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class NotaServiceImpl implements NotaService {

    private final NotaRepository notaRepository;
    private final NegocioRepository negocioRepository;
    private final SafraRepository safraRepository;
    private final ProjetoRepository projetoRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;

    @Override
    public NotaResponse salvar(NotaRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        validarVinculoUnico(request.negocioId(), request.safraId(), request.projetoId());

        Nota nota = new Nota();
        nota.setFazenda(fazenda);
        preencher(nota, request);

        return NotaResponse.de(notaRepository.save(nota));
    }

    @Override
    public NotaResponse atualizar(Long id, NotaRequest request, String numeroUsuarioLogado) {
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nota não encontrada"));
        fazendaAcessoHelper.validarAcesso(nota.getFazenda(), numeroUsuarioLogado);

        validarVinculoUnico(request.negocioId(), request.safraId(), request.projetoId());
        preencher(nota, request);

        return NotaResponse.de(notaRepository.save(nota));
    }

    @Override
    public void excluir(Long id, String numeroUsuarioLogado) {
        Nota nota = notaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nota não encontrada"));
        fazendaAcessoHelper.validarAcesso(nota.getFazenda(), numeroUsuarioLogado);
        notaRepository.delete(nota);
    }

    @Override
    public List<NotaResponse> buscarPorFazenda(Long fazendaId, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return notaRepository.buscarPorFazenda(fazendaId).stream().map(NotaResponse::de).toList();
    }

    private void preencher(Nota nota, NotaRequest request) {
        nota.setTitulo(request.titulo().trim());
        nota.setCorpo(request.corpo() == null || request.corpo().isBlank() ? null : request.corpo().strip());
        aplicarVinculo(nota, request.negocioId(), request.safraId(), request.projetoId());
    }

    private void validarVinculoUnico(Long negocioId, Long safraId, Long projetoId) {
        long preenchidos = Stream.of(negocioId, safraId, projetoId).filter(Objects::nonNull).count();
        if (preenchidos > 1) {
            throw new IllegalArgumentException("Selecione no máximo um vínculo: negócio, safra ou projeto");
        }
    }

    private void aplicarVinculo(Nota nota, Long negocioId, Long safraId, Long projetoId) {
        if (negocioId != null) {
            Negocio negocio = negocioRepository.findById(negocioId)
                    .orElseThrow(() -> new NotFoundException("Negócio não encontrado"));
            nota.setNegocio(negocio);
            nota.setSafra(null);
            nota.setProjeto(null);
        } else if (safraId != null) {
            Safra safra = safraRepository.findById(safraId)
                    .orElseThrow(() -> new NotFoundException("Safra não encontrada"));
            nota.setSafra(safra);
            nota.setNegocio(null);
            nota.setProjeto(null);
        } else if (projetoId != null) {
            Projeto projeto = projetoRepository.findById(projetoId)
                    .orElseThrow(() -> new NotFoundException("Projeto não encontrado"));
            nota.setProjeto(projeto);
            nota.setNegocio(null);
            nota.setSafra(null);
        } else {
            nota.setNegocio(null);
            nota.setSafra(null);
            nota.setProjeto(null);
        }
    }
}