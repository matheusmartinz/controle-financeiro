package com.mfinancas.api.service;

import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.model.Categoria;
import com.mfinancas.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CategoriaService extends SuperServiceSave<Categoria, CategoriaRepository> {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ValidateCategoriaTO validateCategoria;

    public CategoriaService(CategoriaRepository repository) {
        super(repository);
    }

    public CategoriaTO createCategoria(CategoriaTO categoriaTO, UUID usuarioFK) {
        validateCategoria.validarCategoriaRequest(categoriaTO, usuarioFK);
        Categoria categoria = new Categoria(categoriaTO);
        return new CategoriaTO(saveAndFlush(categoria));
    }

//    public List<CategoriaTO> getAllCategorias() {
//        return categoriaRepository.findAll().stream().map(CategoriaTO::new).toList();
//    }

}
