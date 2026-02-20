package com.mfinancas.api.service;

import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.model.Usuario;
import com.mfinancas.api.repository.UsuarioRepository;
import com.mfinancas.api.repository.UsuarioRepositoryCustomImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService extends SuperServiceSave<Usuario, UsuarioRepository> {
    @Autowired
    private ValidateTO validateTO;

    @Autowired
    private UsuarioRepositoryCustomImpl usuarioRepositoryCustomImpl;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository repository) {
        super(repository);
    }

    public UsuarioTO createUsuario(UsuarioTO usuarioTO) {
        validateTO.validateUsuarioTO(usuarioTO);
        Usuario usuario = new Usuario(usuarioTO);
        return new UsuarioTO(save(usuario));
    }

    public UsuarioTO postLogin(UsuarioTO usuarioTO) {
        validateTO.validateUsuarioTO(usuarioTO);
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
}
