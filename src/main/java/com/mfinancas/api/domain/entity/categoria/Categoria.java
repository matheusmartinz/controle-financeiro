package com.mfinancas.api.domain.entity.categoria;

import com.mfinancas.api.TipoCategoria;
import com.mfinancas.api.domain.entity.abstractentity.AbstractEntity;
import com.mfinancas.api.api.dto.categoria.CategoriaDTO;
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


    public Categoria(CategoriaDTO categoriaDTO, UUID uuidUsuario) {
        super();
        this.nome = categoriaDTO.nome();
        this.tipo = categoriaDTO.tipo();
        this.usuarioFK = uuidUsuario;
    }

    public void updateCategoria(String nome, TipoCategoria tipo) {
        this.nome = nome;
        this.tipo = tipo;
    }
}
