package com.mfinancas.api.dataprovider;

import com.mfinancas.api.api.dto.usuario.UsuarioDTO;
import com.mfinancas.api.service.usuario.UsuarioService;
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
                "Pedro Correia",
                "jorge@gmail.com",
                "jorge123"

        );
        return usuarioService.createUsuario(usuarioDTO);
    }

    public UsuarioDTO createUsuarioCustom( String nome,String email, String senha) {
        UsuarioDTO usuarioDTO = new UsuarioDTO(
                UUID.randomUUID(),
                nome,
                email,
                senha
        );

        return usuarioService.createUsuario(usuarioDTO);
    }
}
