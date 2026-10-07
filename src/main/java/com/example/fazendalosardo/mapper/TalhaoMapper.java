package com.example.fazendalosardo.mapper;

import com.example.fazendalosardo.dto.safraDTO.TalhaoRequest;
import com.example.fazendalosardo.dto.safraDTO.TalhaoResponse;
import com.example.fazendalosardo.dto.safraDTO.TalhaoUpdateRequest;
import com.example.fazendalosardo.model.safras.Talhao;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TalhaoMapper {

    @Mapping(target = "ativo", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "safra", ignore = true)
    Talhao toEntity(TalhaoRequest request);

    @Mapping(target = "safraId", source = "safra.id")
    @Mapping(target = "safraNome", source = "safra.nome")
    TalhaoResponse toResponse(Talhao talhao);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "safra", ignore = true)
    @Mapping(target = "ativo", ignore = true)
    void atualizar(TalhaoUpdateRequest request, @MappingTarget Talhao talhao);
}
