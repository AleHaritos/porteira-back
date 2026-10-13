package com.example.fazendalosardo.dto.veiculos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostoRequest(@NotBlank String nome, @NotNull Long fazendaId, String observacao) {}