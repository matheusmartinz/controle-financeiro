package com.mfinancas.api.dto;

import com.mfinancas.api.TipoCategoria;

import java.util.UUID;

public record CategoriaTO(UUID uuidCategoria, String nome, TipoCategoria tipo, UUID usuario) {

};
