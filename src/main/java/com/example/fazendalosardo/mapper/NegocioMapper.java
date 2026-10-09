package com.example.fazendalosardo.mapper;

import com.example.fazendalosardo.dto.negocio.NegocioRequest;
import com.example.fazendalosardo.dto.negocio.NegocioResponse;
import com.example.fazendalosardo.dto.negocio.NegocioUpdateRequest;
import com.example.fazendalosardo.model.Negocio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface NegocioMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fazenda", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    Negocio toEntity(NegocioRequest request);

    @Mapping(target = "fazendaId", source = "fazenda.id")
    @Mapping(target = "fazendaNome", source = "fazenda.nome")
    NegocioResponse toResponse(Negocio negocio);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fazenda", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    void atualizar(NegocioUpdateRequest request, @MappingTarget Negocio negocio);
}