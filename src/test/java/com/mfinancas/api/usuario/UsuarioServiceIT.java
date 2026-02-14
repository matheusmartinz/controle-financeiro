package com.mfinancas.api.usuario;

import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.repository.UsuarioRepository;
import com.mfinancas.api.service.UsuarioService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceIT {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void createUsuario() {
        UsuarioTO usuarioTO = new UsuarioTO(UUID.randomUUID(), "matheus@gmail.com", "blablabla");
        int before = usuarioRepository.findAll().size();

        UsuarioTO usuarioResponse = usuarioService.createUsuario(usuarioTO);

        int after = usuarioRepository.findAll().size();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(usuarioResponse.senha()).isNotEqualTo(null);
            s.assertThat(after).isEqualTo(before + 1);
        });
    }

    @Test
    public void getAllUsuarios() {
        UsuarioTO usuarioTO = new UsuarioTO(UUID.randomUUID(), "matheus@gmail.com", "blablabla");
        usuarioService.createUsuario(usuarioTO);

        List<UsuarioTO> listUsuarios = usuarioService.getAll();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(listUsuarios).isNotNull();
            s.assertThat(listUsuarios.size()).isGreaterThan(0);
        });
    }
}
