package com.mfinancas.api.service;

import com.mfinancas.api.controller.ValidateUsuario;
import com.mfinancas.api.dto.UsuarioTO;
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

    public UsuarioTO createUsuario(UsuarioTO usuarioTO) {
        validateUsuario.validateUsuarioTO(usuarioTO);
        Usuario usuario = new Usuario(usuarioTO);
        return new UsuarioTO(save(usuario));
    }

    public UsuarioTO postLogin(UsuarioTO usuarioTO) {
        validateUsuario.validateUsuarioTO(usuarioTO);
        Usuario userEntity = usuarioRepositoryCustomImpl.findByUser(usuarioTO);
        if (userEntity == null) {
            throw new IsNull("Credenciais incorretos.");
        }
        return new UsuarioTO(userEntity);
    }

    public List<UsuarioTO> getAll() {
        List<Usuario> listUsers = usuarioRepository.findAll();
        return listUsers.stream().map(UsuarioTO::new).toList();
    }

    public void deleteUsuario(UUID uuidUser) {
        if (usuarioRepository.findByUuid(uuidUser) == null) {
            throw new IsNull("Usuário não encontrado.");
        }
        usuarioRepository.deleteByUuid(uuidUser);
    }
}
