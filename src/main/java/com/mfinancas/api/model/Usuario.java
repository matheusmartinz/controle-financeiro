package com.mfinancas.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class Usuario extends AbstractEntity{

    private String nome;

    private String email;

    private String senha;

    @OneToMany(mappedBy = "usuario")
    private List<Receita> receitas;

    @OneToMany(mappedBy = "usuario")
    private List<Despesa> despesas;

    @OneToMany(mappedBy = "usuario")
    private List<Categoria> categorias;
}
