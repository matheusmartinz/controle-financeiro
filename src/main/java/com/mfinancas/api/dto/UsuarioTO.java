package com.mfinancas.api.dto;

import com.mfinancas.api.model.Categoria;
import com.mfinancas.api.model.Despesa;
import com.mfinancas.api.model.Receita;
import com.mfinancas.api.model.Usuario;

import java.util.List;
import java.util.UUID;

public record UsuarioTO(UUID uuidUsuario, String nome,
                        String email,
                        String senha,
                        List<Receita> receitas,
                        List<Despesa> despesas,
                        List<Categoria> categorias) {


    public UsuarioTO(Usuario usuario) {
        this(usuario.getUuid(), usuario.getNome(), usuario.getEmail(), usuario.getSenha(), usuario.getReceitas(), usuario.getDespesas(), usuario.getCategorias());
    }
}
