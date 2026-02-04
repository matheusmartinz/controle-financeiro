package com.mfinancas.api.service;

import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.model.Usuario;
import com.mfinancas.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService extends SuperServiceSave<Usuario, UsuarioRepository> {
    @Autowired
    private ValidateTO validateTO;
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


    public List<UsuarioTO> getAll() {
        return usuarioRepository.findAll().stream().map(UsuarioTO::new).collect(Collectors.toList());
    }
}
