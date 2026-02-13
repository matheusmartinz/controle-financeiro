package com.mfinancas.api.dataprovider;

import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UsuarioCreateDataProvider {
    @Autowired
    private UsuarioService usuarioService;

    UsuarioTO usuarioTO = new UsuarioTO(
            UUID.randomUUID(),
            "Jorge",
            "jorge@gmail.com",
            "jorge123"

    );

    public UsuarioTO createUsuarioTO() {
        return usuarioService.createUsuario(usuarioTO);
    }
}
