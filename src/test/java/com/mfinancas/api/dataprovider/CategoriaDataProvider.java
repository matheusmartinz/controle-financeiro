package com.mfinancas.api.dataprovider;

import com.mfinancas.api.TipoCategoria;
import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class CategoriaDataProvider {
    @Autowired
    private UsuarioDataProvider usuarioDataProvider;

    @Autowired
    private CategoriaService categoriaService;

    public CategoriaTO createCategoria(String nome) {

        UsuarioTO usuario = usuarioDataProvider.createUsuarioTO();

        CategoriaTO categoriaTO = new CategoriaTO(
                UUID.randomUUID(),
                nome,
                TipoCategoria.DESPESA,
                usuario.uuid()
        );

        return categoriaService.createCategoria(categoriaTO, usuario.uuid());
    }
}
