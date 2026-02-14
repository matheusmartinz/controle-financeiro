package com.mfinancas.api.model;

import com.mfinancas.api.TipoCategoria;
import com.mfinancas.api.dto.CategoriaTO;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@Getter
public class Categoria extends AbstractEntity {
    private String nome;

    @Enumerated(EnumType.STRING)
    private TipoCategoria tipo;

    private UUID usuarioFK;


    public Categoria(CategoriaTO categoriaTO) {
        super();
        this.nome = categoriaTO.descricao();
        this.tipo = categoriaTO.tipo();
        this.usuarioFK = categoriaTO.usuarioFK();
    }
}
