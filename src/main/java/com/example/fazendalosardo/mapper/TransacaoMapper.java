package com.example.fazendalosardo.mapper;

import com.example.fazendalosardo.dto.financeiro.TransacaoRequest;
import com.example.fazendalosardo.dto.financeiro.TransacaoResponse;
import com.example.fazendalosardo.dto.financeiro.TransacaoUpdateRequest;
import com.example.fazendalosardo.model.Transacao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TransacaoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "fazenda", ignore = true)
    @Mapping(target = "negocio", ignore = true)
    @Mapping(target = "safra", ignore = true)
    Transacao toEntity(TransacaoRequest request);

    @Mapping(target = "fazendaId", source = "fazenda.id")
    @Mapping(target = "fazendaNome", source = "fazenda.nome")
    @Mapping(target = "negocioId", source = "negocio.id")
    @Mapping(target = "negocioNome", source = "negocio.nome")
    @Mapping(target = "safraId", source = "safra.id")
    @Mapping(target = "safraNome", source = "safra.nome")
    TransacaoResponse toResponse(Transacao transacao);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "fazenda", ignore = true)
    @Mapping(target = "negocio", ignore = true)
    @Mapping(target = "safra", ignore = true)
    void atualizar(TransacaoUpdateRequest request, @MappingTarget Transacao transacao);
}