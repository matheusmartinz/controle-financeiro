package com.mfinancas.api.model;

import com.mfinancas.api.TipoCategoria;
import jakarta.persistence.*;

@Entity
public class Categoria extends AbstractEntity{
    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoCategoria tipo; // RECEITA ou DESPESA

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
