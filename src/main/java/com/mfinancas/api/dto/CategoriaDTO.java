package com.mfinancas.api.dto;

import com.mfinancas.api.TipoCategoria;
import com.mfinancas.api.model.Categoria;

import java.util.UUID;

public record CategoriaDTO(UUID uuidCategoria, String nome, TipoCategoria tipo, UUID usuarioFK) {

    public CategoriaDTO(Categoria categoria) {
        this(
                categoria.getUuid(),
                categoria.getNome(),
                categoria.getTipo(),
                categoria.getUsuarioFK()
        );
    }
};
