package com.mfinancas.api.service;

import com.mfinancas.api.controller.ValidateCategoria;
import com.mfinancas.api.dto.CategoriaDTO;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.model.Categoria;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoriaService extends SuperServiceSave<Categoria, CategoriaRepository> {
    @Autowired
    private ValidateCategoria validateCategoria;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository repository) {
        super(repository);
    }

    public CategoriaDTO createCategoria(CategoriaDTO categoriaDTO, UUID uuidUsuario) {
        validateCategoria.validarCategoriaRequest(categoriaDTO, uuidUsuario);
        Categoria categoria = new Categoria(categoriaDTO, uuidUsuario);
        return new CategoriaDTO(saveAndFlush(categoria));
    }

    public @Nullable List<CategoriaDTO> getAllCategorias() {
        List<Categoria> toReturnEntity = categoriaRepository.findAll();
        return toReturnEntity.stream().map(CategoriaDTO::new).toList();
    }

    public CategoriaDTO updateCategoria(CategoriaDTO categoriaDTO, UUID uuidCategoria) {
        Categoria entity = categoriaRepository.findByUuid(uuidCategoria);
        if (entity == null) {
            throw new IsNull("Categoria não encontrada.");
        }
        validateCategoria.validarCategoriaRequest(categoriaDTO, categoriaDTO.usuarioFK());
        entity.updateCategoria(categoriaDTO.nome(), categoriaDTO.tipo());
        return new CategoriaDTO(categoriaRepository.save(entity));
    }

    public void deleteCategoria(UUID uuidCategoria) {
        if (categoriaRepository.findByUuid(uuidCategoria) == null) {
            throw new IsNull("Categoria não encontrada.");
        }
        categoriaRepository.deleteByUuid(uuidCategoria);
    }
}
