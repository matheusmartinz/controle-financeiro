package com.mfinancas.api.service;

import com.mfinancas.api.controller.ValidateUsuario;
import com.mfinancas.api.dto.UsuarioDTO;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.model.Usuario;
import com.mfinancas.api.repository.UsuarioRepository;
import com.mfinancas.api.repository.UsuarioRepositoryCustomImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService extends SuperServiceSave<Usuario, UsuarioRepository> {
    @Autowired
    private ValidateUsuario validateUsuario;

    @Autowired
    private UsuarioRepositoryCustomImpl usuarioRepositoryCustomImpl;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public UsuarioService(UsuarioRepository repository) {
        super(repository);
    }

    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        validateUsuario.validateUsuarioTO(usuarioDTO);
        Usuario usuario = new Usuario(usuarioDTO);
        return new UsuarioDTO(save(usuario));
    }

    public UsuarioDTO postLogin(UsuarioDTO usuarioDTO) {
        validateUsuario.validateUsuarioTO(usuarioDTO);
        Usuario userEntity = usuarioRepositoryCustomImpl.findByUser(usuarioDTO);
        if (userEntity == null) {
            throw new IsNull("Credenciais incorretos.");
        }
        return new UsuarioDTO(userEntity);
    }

    public List<UsuarioDTO> getAll() {
        List<Usuario> listUsers = usuarioRepository.findAll();
        return listUsers.stream().map(UsuarioDTO::new).toList();
    }

    public void deleteUsuario(UUID uuidUser) {
        if (usuarioRepository.findByUuid(uuidUser) == null) {
            throw new IsNull("Usuário não encontrado.");
        }
        usuarioRepository.deleteByUuid(uuidUser);
    }
}
