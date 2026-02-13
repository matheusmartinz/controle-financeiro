package com.mfinancas.api.dto;

import com.mfinancas.api.TipoCategoria;
import com.mfinancas.api.model.Categoria;

import java.util.UUID;

public record CategoriaTO(UUID uuidCategoria, String descricao, TipoCategoria tipo) {

    public CategoriaTO(Categoria categoria) {
        this(
                categoria.getUuid(),
                categoria.getNome(),
                categoria.getTipo()
        );
    }
};
