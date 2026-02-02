package com.mfinancas.api.usuario;

import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.service.UsuarioService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioServiceIT {

    @Autowired
    private UsuarioService usuarioService;

    @Test
    public void createUsuario(){
        UsuarioTO usuarioTO = new UsuarioTO(UUID.randomUUID(),"João","matheus@gmail.com","blablabla",null, null, null);

        UsuarioTO usuarioResponse = usuarioService.createUsuario(usuarioTO);

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(usuarioResponse.nome()).isNotEqualTo(null);
            s.assertThat(usuarioResponse.senha()).isNotEqualTo(null);
            s.assertThat(usuarioResponse.categorias()).isNull();
            s.assertThat(usuarioResponse.receitas()).isNull();
            s.assertThat(usuarioResponse.despesas()).isNull();
        });
    }
}
