package com.example.fazendalosardo.service.impl;

import com.example.fazendalosardo.dto.animais.*;
import com.example.fazendalosardo.exception.BusinessException;
import com.example.fazendalosardo.exception.NotFoundException;
import com.example.fazendalosardo.model.*;
import com.example.fazendalosardo.model.enums.TipoAnimal;
import com.example.fazendalosardo.repository.animais.AnimalRepository;
import com.example.fazendalosardo.repository.animais.LoteRepository;
import com.example.fazendalosardo.repository.animais.OperacaoVendaRepository;
import com.example.fazendalosardo.repository.animais.PiqueteRepository;
import com.example.fazendalosardo.service.AnimalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AnimalServiceImpl implements AnimalService {

    private final AnimalRepository animalRepository;
    private final LoteRepository loteRepository;
    private final PiqueteRepository piqueteRepository;
    private final OperacaoVendaRepository operacaoVendaRepository;
    private final FazendaAcessoHelper fazendaAcessoHelper;
    private final AnimalCustoHelper animalCustoHelper;

    @Override
    @Transactional
    public List<AnimalResponse> salvarEmMassa(AnimalEmMassaRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        Piquete piquete = piqueteRepository.findById(request.piqueteId())
                .orElseThrow(() -> new NotFoundException("Piquete não encontrado"));

        Set<String> brincosReservados = new HashSet<>();

        List<Animal> animais = request.animais().stream().map(item -> {
            Lote lote = null;
            if (item.loteId() != null) {
                lote = loteRepository.findById(item.loteId())
                        .orElseThrow(() -> new NotFoundException("Lote não encontrado"));
            }

            Animal animal = new Animal();
            animal.setTipo(item.tipo());
            animal.setLote(lote);
            animal.setPiquete(piquete);
            animal.setFazenda(fazenda);
            animal.setDescricao(item.descricao());
            animal.setSexo(item.sexo());
            animal.setPeso(item.peso());
            animal.setDataNascimento(item.dataNascimento());
            animal.setRaca(item.raca());
            animal.setValorCompra(item.valorCompra());

            String brinco = gerarBrincoDisponivel(fazenda.getId(), brincosReservados);
            brincosReservados.add(brinco);
            animal.setBrinco(brinco);

            return animal;
        }).toList();

        return animalRepository.saveAll(animais).stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional
    public AnimalResponse salvar(AnimalRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        Piquete piquete = piqueteRepository.findById(request.piqueteId())
                .orElseThrow(() -> new NotFoundException("Piquete não encontrado"));

        Lote lote = null;
        if (request.loteId() != null) {
            lote = loteRepository.findById(request.loteId())
                    .orElseThrow(() -> new NotFoundException("Lote não encontrado"));
        }

        Animal animal = new Animal();
        animal.setTipo(request.tipo());
        animal.setLote(lote);
        animal.setPiquete(piquete);
        animal.setFazenda(fazenda);
        animal.setDescricao(request.descricao());
        animal.setSexo(request.sexo());
        animal.setDataNascimento(request.dataNascimento());
        animal.setDataCompra(request.dataCompra());
        animal.setValorCompra(request.valorCompra());
        animal.setPeso(request.peso());
        animal.setRaca(request.raca());

        if (request.brinco() != null && !request.brinco().isBlank()) {
            if (animalRepository.existsByFazendaIdAndBrinco(fazenda.getId(), request.brinco())) {
                throw new BusinessException("Já existe um animal com esse brinco nesta fazenda");
            }
            animal.setBrinco(request.brinco());
        } else {
            animal.setBrinco(gerarBrincoDisponivel(fazenda.getId()));
        }

        Animal salvo = animalRepository.save(animal);

        return toResponse(salvo);
    }

    @Override
    public Page<OperacaoVendaResponse> listarOperacoesVenda(Long fazendaId, LocalDate dataInicio, LocalDate dataFim,
                                                            int page, int size, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        Pageable pageable = PageRequest.of(page, size);
        return operacaoVendaRepository.buscarPorFazendaEPeriodo(fazendaId, dataInicio, dataFim, pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional
    public AnimalResponse atualizar(Long id, AnimalRequest request, String numeroUsuarioLogado) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Animal não encontrado"));

        fazendaAcessoHelper.validarAcesso(animal.getFazenda(), numeroUsuarioLogado);

        Piquete piquete = piqueteRepository.findById(request.piqueteId())
                .orElseThrow(() -> new NotFoundException("Piquete não encontrado"));

        Lote lote = null;
        if (request.loteId() != null) {
            lote = loteRepository.findById(request.loteId())
                    .orElseThrow(() -> new NotFoundException("Lote não encontrado"));
        }

        animal.setTipo(request.tipo());
        animal.setLote(lote);
        animal.setPiquete(piquete);
        animal.setDescricao(request.descricao());
        animal.setSexo(request.sexo());
        animal.setDataNascimento(request.dataNascimento());
        animal.setDataCompra(request.dataCompra());
        animal.setValorCompra(request.valorCompra());
        animal.setPeso(request.peso());
        animal.setRaca(request.raca());

        if (request.brinco() != null && !request.brinco().isBlank()
                && !request.brinco().equals(animal.getBrinco())) {
            if (animalRepository.existsByFazendaIdAndBrincoAndIdNot(animal.getFazenda().getId(), request.brinco(), animal.getId())) {
                throw new BusinessException("Já existe um animal com esse brinco nesta fazenda");
            }
            animal.setBrinco(request.brinco());
        }

        return toResponse(animal);
    }

    private String gerarBrincoDisponivel(Long fazendaId) {
        return gerarBrincoDisponivel(fazendaId, Set.of());
    }

    private String gerarBrincoDisponivel(Long fazendaId, Set<String> reservadosNoLote) {
        long candidato = animalRepository.countByFazendaId(fazendaId) + 1;
        String brinco = String.valueOf(candidato);

        while (reservadosNoLote.contains(brinco) || animalRepository.existsByFazendaIdAndBrinco(fazendaId, brinco)) {
            candidato++;
            brinco = String.valueOf(candidato);
        }

        return brinco;
    }

    @Override
    public Page<AnimalResponse> listarPorFazenda(Long fazendaId, Long piqueteId, Long loteId, TipoAnimal tipo,
                                                 Pageable pageable, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return animalRepository.buscarComFiltros(fazendaId, piqueteId, loteId, tipo, pageable)
                .map(this::toResponse);
    }

    @Override
    @Transactional
    public void excluir(Long id, String numeroUsuarioLogado) {
        Animal animal = animalRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Animal não encontrado"));
        fazendaAcessoHelper.validarAcesso(animal.getFazenda(), numeroUsuarioLogado);
        animalRepository.delete(animal);
    }

    @Override
    @Transactional
    public void transferirAnimais(TransferenciaAnimalRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        Piquete piqueteDestino = piqueteRepository.findById(request.piqueteDestinoId())
                .orElseThrow(() -> new NotFoundException("Piquete de destino não encontrado"));

        if (!piqueteDestino.getFazenda().getId().equals(fazenda.getId())) {
            throw new BusinessException("O piquete de destino não pertence à fazenda informada");
        }

        List<Animal> animais = animalRepository.findAllById(request.animalIds());

        if (animais.size() != request.animalIds().size()) {
            throw new NotFoundException("Um ou mais animais não foram encontrados");
        }

        boolean algumDeOutraFazenda = animais.stream()
                .anyMatch(a -> !a.getFazenda().getId().equals(fazenda.getId()));
        if (algumDeOutraFazenda) {
            throw new BusinessException("Todos os animais devem pertencer à fazenda informada");
        }

        animais.forEach(a -> a.setPiquete(piqueteDestino));
    }

    @Override
    @Transactional
    public OperacaoVendaResponse venderAnimais(VendaAnimalRequest request, String numeroUsuarioLogado) {
        Fazenda fazenda = fazendaAcessoHelper.buscarComAcesso(request.fazendaId(), numeroUsuarioLogado);

        List<Animal> animais = animalRepository.findAllById(request.animalIds());

        if (animais.size() != request.animalIds().size()) {
            throw new NotFoundException("Um ou mais animais não foram encontrados");
        }

        boolean algumDeOutraFazenda = animais.stream()
                .anyMatch(a -> !a.getFazenda().getId().equals(fazenda.getId()));
        if (algumDeOutraFazenda) {
            throw new BusinessException("Todos os animais devem pertencer à fazenda informada");
        }

        OperacaoVenda operacao = new OperacaoVenda();
        operacao.setFazenda(fazenda);
        operacao.setData(request.data());
        operacao.setDescricao(request.descricao());
        operacao.setValorVenda(request.valor());

        BigDecimal custoTotalOperacao = BigDecimal.ZERO;

        for (Animal animal : animais) {
            AnimalCustoResponse custo = animalCustoHelper.calcularCusto(animal);

            OperacaoVendaItem item = new OperacaoVendaItem();
            item.setOperacaoVenda(operacao);
            item.setAnimal(animal);
            item.setCustoVacinacao(custo.custoVacinacao());
            item.setCustoAlimentacao(custo.custoAlimentacao());
            item.setValorCompra(custo.valorCompra());
            item.setCustoTotal(custo.custoTotal());

            operacao.getItens().add(item);
            custoTotalOperacao = custoTotalOperacao.add(custo.custoTotal());

            animal.setAtivo(false);
        }

        operacao.setCustoTotal(custoTotalOperacao);
        operacao.setLucro(request.valor().subtract(custoTotalOperacao));

        operacaoVendaRepository.save(operacao);

        return toResponse(operacao);
    }

    @Override
    public List<OperacaoVendaResponse> listarOperacoesVenda(Long fazendaId, String numeroUsuarioLogado) {
        fazendaAcessoHelper.buscarComAcesso(fazendaId, numeroUsuarioLogado);
        return operacaoVendaRepository.buscarPorFazenda(fazendaId).stream().map(this::toResponse).toList();
    }

    @Override
    public AnimalCustoResponse calcularCusto(Long animalId, String numeroUsuarioLogado) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new NotFoundException("Animal não encontrado"));
        fazendaAcessoHelper.validarAcesso(animal.getFazenda(), numeroUsuarioLogado);
        return animalCustoHelper.calcularCusto(animal);
    }

    @Override
    public List<HistoricoCuidadoAnimalResponse> listarHistorico(Long animalId, String numeroUsuarioLogado) {
        Animal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new NotFoundException("Animal não encontrado"));
        fazendaAcessoHelper.validarAcesso(animal.getFazenda(), numeroUsuarioLogado);
        return animalCustoHelper.listarHistorico(animal);
    }

    private AnimalResponse toResponse(Animal animal) {
        Integer idadeMeses = animal.getDataNascimento() != null
                ? (int) ChronoUnit.MONTHS.between(animal.getDataNascimento(), LocalDate.now())
                : null;

        return new AnimalResponse(
                animal.getId(),
                animal.getTipo(),
                animal.getLote() != null ? animal.getLote().getId() : null,
                animal.getLote() != null ? animal.getLote().getNome() : null,
                animal.getPiquete().getId(),
                animal.getPiquete().getNome(),
                animal.getDescricao(),
                animal.getAtivo(),
                animal.getSexo(),
                animal.getDataNascimento(),
                idadeMeses,
                animal.getDataCompra(),
                animal.getValorCompra(),
                animal.getPeso(),
                animal.getBrinco(),
                animal.getRaca()
        );
    }

    private OperacaoVendaResponse toResponse(OperacaoVenda operacao) {
        List<OperacaoVendaItemResponse> itens = operacao.getItens().stream()
                .map(item -> new OperacaoVendaItemResponse(
                        item.getAnimal() != null ? item.getAnimal().getId() : null,
                        item.getAnimal() != null ? item.getAnimal().getTipo().name() : null,
                        item.getAnimal() != null ? item.getAnimal().getDescricao() : null,
                        item.getAnimal() != null ? item.getAnimal().getBrinco() : null,
                        item.getCustoVacinacao(),
                        item.getCustoAlimentacao(),
                        item.getValorCompra(),
                        item.getCustoTotal()
                ))
                .toList();

        return new OperacaoVendaResponse(
                operacao.getId(),
                operacao.getData(),
                operacao.getDescricao(),
                operacao.getValorVenda(),
                operacao.getCustoTotal(),
                operacao.getLucro(),
                itens
        );
    }
}