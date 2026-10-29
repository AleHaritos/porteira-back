package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.calendario.EventoCalendarioRequest;
import com.example.fazendalosardo.dto.calendario.EventoCalendarioResponse;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.EventoCalendario;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.repository.EventoCalendarioRepository;
import com.example.fazendalosardo.service.EventoCalendarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EventoCalendarioServiceImpl implements EventoCalendarioService {

    private final EventoCalendarioRepository eventoCalendarioRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;

    @Override
    public EventoCalendarioResponse salvar(EventoCalendarioRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        EventoCalendario evento = new EventoCalendario();
        evento.setFazenda(fazenda);
        evento.setTitulo(request.titulo());
        evento.setDescricao(request.descricao());
        evento.setData(request.data());
        evento.setHoraInicio(request.horaInicio());
        evento.setHoraFim(request.horaFim());

        return EventoCalendarioResponse.de(eventoCalendarioRepository.save(evento));
    }

    @Override
    public EventoCalendarioResponse atualizar(Long id, EventoCalendarioRequest request, String numeroUsuarioLogado) {
        EventoCalendario evento = eventoCalendarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Evento não encontrado"));
        fazendaAcessoHelper.validarAcesso(evento.getFazenda(), numeroUsuarioLogado);

        evento.setTitulo(request.titulo());
        evento.setDescricao(request.descricao());
        evento.setData(request.data());
        evento.setHoraInicio(request.horaInicio());
        evento.setHoraFim(request.horaFim());

        return EventoCalendarioResponse.de(eventoCalendarioRepository.save(evento));
    }

    @Override
    public void excluir(Long id, String numeroUsuarioLogado) {
        EventoCalendario evento = eventoCalendarioRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Evento não encontrado"));
        fazendaAcessoHelper.validarAcesso(evento.getFazenda(), numeroUsuarioLogado);
        eventoCalendarioRepository.delete(evento);
    }

    @Override
    public List<EventoCalendarioResponse> buscarPorFazendaEPeriodo(Long fazendaId, LocalDate dataInicio, LocalDate dataFim, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return eventoCalendarioRepository.buscarPorFazendaEPeriodo(fazendaId, dataInicio, dataFim)
                .stream().map(EventoCalendarioResponse::de).toList();
    }
}