package com.example.fazendalosardo.mapper;

import com.example.fazendalosardo.dto.safraDTO.SafraRequest;
import com.example.fazendalosardo.dto.safraDTO.SafraResponse;
import com.example.fazendalosardo.model.safras.Safra;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SafraMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fazenda", ignore = true)
    Safra toEntity(SafraRequest request);

    @Mapping(target = "fazendaId", source = "fazenda.id")
    @Mapping(target = "fazendaNome", source = "fazenda.nome")
    SafraResponse toResponse(Safra safra);

}
