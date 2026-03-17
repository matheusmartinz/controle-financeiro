package com.mfinancas.api.dataprovider;

import com.mfinancas.api.dto.UsuarioDTO;
import com.mfinancas.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UsuarioDataProvider {
    @Autowired
    private UsuarioService usuarioService;

    public UsuarioDTO createUsuarioTO() {
        UsuarioDTO usuarioDTO = new UsuarioDTO(
                UUID.randomUUID(),
                "jorge@gmail.com",
                "jorge123"

        );
        return usuarioService.createUsuario(usuarioDTO);
    }

    public UsuarioDTO createUsuarioCustom(String email, String senha) {
        UsuarioDTO usuarioDTO = new UsuarioDTO(
                UUID.randomUUID(),
                email,
                senha
        );

        return usuarioService.createUsuario(usuarioDTO);
    }
}
