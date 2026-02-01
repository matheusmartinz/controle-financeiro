package com.mfinancas.api.dto;

import com.mfinancas.api.model.Categoria;
import com.mfinancas.api.model.Despesa;
import com.mfinancas.api.model.Receita;

import java.util.List;

public record UsuarioTO(String nome,
                        String email,
                        String senha,
                        List<Receita> receitas,
                        List<Despesa> despesas,
                        List<Categoria> categorias) {
}
