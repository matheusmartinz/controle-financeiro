package com.mfinancas.api.service;

import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {
    @Autowired
    private ValidateTO validateTO;
    public UsuarioTO createUsuario(UsuarioTO usuarioTO) {
        validateTO.validateUsuarioTO(usuarioTO);
        Usuario usuario = new Usuario(usuarioTO);
        return new UsuarioTO(usuario);
    }
}
