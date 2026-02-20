package com.mfinancas.api.service;

import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.model.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoriaService extends SuperServiceSave<Categoria, CategoriaRepository> {
    @Autowired
    private ValidateCategoriaTO validateCategoria;

    public CategoriaService(CategoriaRepository repository) {
        super(repository);
    }

    public CategoriaTO createCategoria(CategoriaTO categoriaTO, UUID uuidUsuario) {
        validateCategoria.validarCategoriaRequest(categoriaTO, uuidUsuario);
        Categoria categoria = new Categoria(categoriaTO, uuidUsuario);
        return new CategoriaTO(saveAndFlush(categoria));
    }

}
