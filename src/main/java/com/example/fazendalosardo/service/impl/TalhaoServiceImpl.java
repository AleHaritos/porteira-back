package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.safraDTO.TalhaoRequest;
import com.example.fazendalosardo.dto.safraDTO.TalhaoResponse;
import com.example.fazendalosardo.dto.safraDTO.TalhaoUpdateRequest;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.mapper.TalhaoMapper;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.model.safras.Talhao;
import com.example.fazendalosardo.repository.safras.SafraRepository;
import com.example.fazendalosardo.repository.safras.TalhaoRepository;
import com.example.fazendalosardo.service.TalhaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TalhaoServiceImpl implements TalhaoService {

    private final TalhaoRepository talhaoRepository;
    private final SafraRepository safraRepository;
    private final TalhaoMapper talhaoMapper;

    @Override
    @Transactional
    public TalhaoResponse salvar(TalhaoRequest request) {
        Safra safra = safraRepository.findById(request.safraId())
                .orElseThrow(() -> new NotFoundException("Safra não encontrada"));

        validarAreaDisponivel(safra, request.areaHectares());

        Talhao talhao = talhaoMapper.toEntity(request);
        talhao.setSafra(safra);

        return talhaoMapper.toResponse(talhaoRepository.save(talhao));
    }

    private void validarAreaDisponivel(Safra safra, BigDecimal areaNovoTalhao) {
        BigDecimal areaJaUsada = talhaoRepository.somarAreaPorSafra(safra.getId());
        BigDecimal areaTotalComNovo = areaJaUsada.add(areaNovoTalhao);

        if (areaTotalComNovo.compareTo(safra.getAreaTotal()) > 0) {
            BigDecimal areaDisponivel = safra.getAreaTotal().subtract(areaJaUsada);
            throw new BusinessException(
                    "Área do talhão excede o disponível na safra. Disponível: " + areaDisponivel + " ha"
            );
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TalhaoResponse> buscarPorSafra(Long safraId, Pageable pageable) {
        return talhaoRepository.findBySafraId(safraId, pageable)
                .map(talhaoMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TalhaoResponse> listarPorSafra(Long safraId) {
        return talhaoRepository.findBySafraIdAndAtivoTrueOrderByNomeAsc(safraId)
                .stream()
                .map(talhaoMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void desativar(Long id) {
        Talhao talhao = talhaoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Talhão não encontrado"));
        talhao.setAtivo(false);
        talhaoRepository.save(talhao);
    }

    @Override
    @Transactional
    public void reativar(Long id) {
        Talhao talhao = talhaoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Talhão não encontrado"));
        talhao.setAtivo(true);
        talhaoRepository.save(talhao);
    }

    @Override
    @Transactional
    public TalhaoResponse atualizar(Long id, TalhaoUpdateRequest request) {
        Talhao talhao = talhaoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Talhão não encontrado"));

        BigDecimal somaOutrosTalhoes = talhaoRepository.somarAreaPorSafraExcluindoTalhao(
                talhao.getSafra().getId(), talhao.getId()
        );

        BigDecimal novaSoma = somaOutrosTalhoes.add(request.areaHectares());

        if (novaSoma.compareTo(talhao.getSafra().getAreaTotal()) > 0) {
            throw new BusinessException(
                    "A soma das áreas dos talhões não pode exceder a área total da safra (%s ha)"
                            .formatted(talhao.getSafra().getAreaTotal())
            );
        }

        talhaoMapper.atualizar(request, talhao);

        return talhaoMapper.toResponse(talhaoRepository.save(talhao));
    }
}