package com.mfinancas.api.dto;

import com.mfinancas.api.model.Categoria;
import com.mfinancas.api.model.Usuario;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public record UsuarioDTO(UUID uuid,
                         String nome,
                         String email,
                         String senha,
                         List<CategoriaDTO> categorias,
                         List<DespesaDTO> despesas,
                         List<ReceitaDTO> receitas) {


    public UsuarioDTO(Usuario usuario) {
        this(usuario.getUuid(),
                usuario.getNome(),
                usuario.getEmail(),
                "SEGREDO",
                usuario.getCategorias(),
                usuario.getDespesas(),
                usuario.getReceitas());
    }

    public static List<Categoria> listOf(List<CategoriaDTO> categorias) {
        return categorias.stream().map(CategoriaDTO::new).toList();
    }
}
