package com.example.fazendalosardo.mapper;

import com.example.fazendalosardo.dto.fazendaDTO.FazendaRequest;
import com.example.fazendalosardo.dto.fazendaDTO.FazendaResponse;
import com.example.fazendalosardo.dto.fazendaDTO.FazendaResumoResponse;
import com.example.fazendalosardo.dto.UsuarioDTO;
import com.example.fazendalosardo.dto.fazendaDTO.FazendaUpdateRequest;
import com.example.fazendalosardo.model.Fazenda;
import com.example.fazendalosardo.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface FazendaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dono", ignore = true)
    @Mapping(target = "colaboradores", ignore = true)
    Fazenda toEntity(FazendaRequest request);

    @Mapping(target = "dono", source = "fazenda.dono")
    @Mapping(target = "colaboradores", source = "colaboradores")
    FazendaResponse toResponse(Fazenda fazenda, List<UsuarioDTO> colaboradores);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dono", ignore = true)
    @Mapping(target = "colaboradores", ignore = true)
    void atualizar(FazendaUpdateRequest request, @MappingTarget Fazenda fazenda);

    FazendaResumoResponse toResumoResponse(Fazenda fazenda);

    UsuarioDTO toResumo(Usuario usuario);
}