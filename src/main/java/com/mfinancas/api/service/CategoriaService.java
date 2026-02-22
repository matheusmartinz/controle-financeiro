package com.mfinancas.api.service;

import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.model.Categoria;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoriaService extends SuperServiceSave<Categoria, CategoriaRepository> {
    @Autowired
    private ValidateCategoriaTO validateCategoria;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository repository) {
        super(repository);
    }

    public CategoriaTO createCategoria(CategoriaTO categoriaTO, UUID uuidUsuario) {
        validateCategoria.validarCategoriaRequest(categoriaTO, uuidUsuario);
        Categoria categoria = new Categoria(categoriaTO, uuidUsuario);
        return new CategoriaTO(saveAndFlush(categoria));
    }

    public @Nullable List<CategoriaTO> getAllCategorias() {
        List<Categoria> toReturnEntity = categoriaRepository.findAll();
        return toReturnEntity.stream().map(CategoriaTO::new).toList();
    }
}
