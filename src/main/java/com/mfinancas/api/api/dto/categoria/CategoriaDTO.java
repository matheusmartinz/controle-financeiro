package com.mfinancas.api.api.dto.categoria;

import com.mfinancas.api.TipoCategoria;
import com.mfinancas.api.domain.entity.categoria.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CategoriaDTO(UUID uuidCategoria,
                           @NotBlank(message = "Obrigatório informar o descricao da categoria.") String nome,
                           @NotNull(message = "Obrigatório informar o  tipo da categoria." ) TipoCategoria tipo,
                           @NotNull UUID usuarioFK) {

    public CategoriaDTO(Categoria categoria) {
        this(
                categoria.getUuid(),
                categoria.getNome(),
                categoria.getTipo(),
                categoria.getUsuarioFK()
        );
    }
};
