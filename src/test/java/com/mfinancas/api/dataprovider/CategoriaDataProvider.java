package com.mfinancas.api.dataprovider;

import com.mfinancas.api.TipoCategoria;
import com.mfinancas.api.api.dto.categoria.CategoriaDTO;
import com.mfinancas.api.api.dto.usuario.UsuarioDTO;
import com.mfinancas.api.service.categoria.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CategoriaDataProvider {
    @Autowired
    private UsuarioDataProvider usuarioDataProvider;

    @Autowired
    private CategoriaService categoriaService;

    public CategoriaDTO createCategoria(String nome) {

        UsuarioDTO usuario = usuarioDataProvider.createUsuarioTO();

        CategoriaDTO categoriaDTO = new CategoriaDTO(
                UUID.randomUUID(),
                nome,
                TipoCategoria.DESPESA,
                usuario.uuid()
        );

        return categoriaService.createCategoria(categoriaDTO, usuario.uuid());
    }
}
