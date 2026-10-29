package com.example.fazendalosardo.mapper;

import com.example.fazendalosardo.dto.projeto.ProjetoRequest;
import com.example.fazendalosardo.dto.projeto.ProjetoResponse;
import com.example.fazendalosardo.dto.projeto.ProjetoUpdateRequest;
import com.example.fazendalosardo.model.Projeto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProjetoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fazenda", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    Projeto toEntity(ProjetoRequest request);

    @Mapping(target = "fazendaId", source = "fazenda.id")
    @Mapping(target = "fazendaNome", source = "fazenda.nome")
    ProjetoResponse toResponse(Projeto projeto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fazenda", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    void atualizar(ProjetoUpdateRequest request, @MappingTarget Projeto projeto);
}