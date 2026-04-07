package com.mfinancas.api.service.categoria;

import com.mfinancas.api.api.dto.categoria.CategoriaDTO;
import com.mfinancas.api.domain.entity.categoria.Categoria;
import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.repository.categoria.CategoriaRepository;
import com.mfinancas.api.repository.usuario.UsuarioRepository;
import com.mfinancas.api.service.superservice.SuperServiceSave;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CategoriaService extends SuperServiceSave<Categoria, CategoriaRepository> {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public CategoriaService(CategoriaRepository repository) {
        super(repository);
    }

    public CategoriaDTO createCategoria(CategoriaDTO categoriaDTO, UUID uuidUsuario) {
        if (usuarioRepository.findByUuid(uuidUsuario) == null) {
            throw new IsNull("Usuário não encontrado.");
        }
        if (categoriaRepository.existsByNome(categoriaDTO.nome())) {
            throw new FailedConditional("Já existe um nome cadastrado com essa categoria.");
        }
        Categoria categoria = new Categoria(categoriaDTO, uuidUsuario);
        return new CategoriaDTO(saveAndFlush(categoria));
    }

    public List<CategoriaDTO> getAllCategorias() {
        List<Categoria> toReturnEntity = categoriaRepository.findAll();
        return toReturnEntity.stream().map(CategoriaDTO::new).toList();
    }

    public CategoriaDTO updateCategoria(CategoriaDTO categoriaDTO, UUID uuidCategoria) {
        Categoria entity = categoriaRepository.findByUuid(uuidCategoria);
        if (entity == null) {
            throw new IsNull("Categoria não encontrada.");
        }
        if (categoriaRepository.existsByNome(categoriaDTO.nome())) {
            throw new FailedConditional("Já existe um nome cadastrado com essa categoria.");
        }
        if (usuarioRepository.findByUuid(categoriaDTO.usuarioFK()) == null) {
            throw new IsNull("Usuário não encontrado.");
        }
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
