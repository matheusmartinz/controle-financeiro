package com.mfinancas.api.dataprovider;

import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UsuarioDataProvider {
    @Autowired
    private UsuarioService usuarioService;

    public UsuarioTO createUsuarioTO() {
        UsuarioTO usuarioTO = new UsuarioTO(
                UUID.randomUUID(),
                "jorge@gmail.com",
                "jorge123"

        );
        return usuarioService.createUsuario(usuarioTO);
    }
}
