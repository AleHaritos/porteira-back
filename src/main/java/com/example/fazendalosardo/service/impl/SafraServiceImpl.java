package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.safraDTO.SafraRequest;
import com.example.fazendalosardo.dto.safraDTO.SafraResponse;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.mapper.SafraMapper;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.enums.StatusSafra;
import com.example.fazendalosardo.model.safras.Safra;
import com.example.fazendalosardo.repository.FazendaRepository;
import com.example.fazendalosardo.repository.safras.SafraRepository;
import com.example.fazendalosardo.service.SafraService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SafraServiceImpl implements SafraService {

    private final SafraRepository safraRepository;
    private final FazendaRepository fazendaRepository;
    private final SafraMapper safraMapper;

    @Override
    @Transactional
    public SafraResponse salvar(SafraRequest request) {
        Fazenda fazenda = fazendaRepository.findById(request.fazendaId())
                .orElseThrow(() -> new NotFoundException("Fazenda não encontrada"));

        Safra safra = safraMapper.toEntity(request);
        safra.setFazenda(fazenda);

        if (request.status() == null) {
            safra.setStatus(StatusSafra.PLANEJAMENTO);
        }

        return safraMapper.toResponse(safraRepository.save(safra));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SafraResponse> buscarPorFazenda(Long fazendaId, LocalDate dataInicioDe, LocalDate dataInicioAte, Pageable pageable) {
        return safraRepository
                .buscarPorFazenda(fazendaId, StatusSafra.ENCERRADO, dataInicioDe, dataInicioAte, pageable)
                .map(safraMapper::toResponse);
    }
}
