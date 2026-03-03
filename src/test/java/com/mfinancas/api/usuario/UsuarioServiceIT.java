package com.mfinancas.api.usuario;

import com.mfinancas.api.dataprovider.UsuarioDataProvider;
import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.repository.UsuarioRepository;
import com.mfinancas.api.service.UsuarioService;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
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

    @Autowired
    private UsuarioDataProvider usuarioDataProvider;

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

    @Test
    @SneakyThrows
    public void camposNulos(){
        UsuarioTO usuarioTO = new UsuarioTO(UUID.randomUUID(), null, null);

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> usuarioService.postLogin(usuarioTO))
                    .isInstanceOf(FailedConditional.class)
                    .hasMessage("Obrigatório inserir todos os campos.");
        });
    }

    @Test
    @SneakyThrows
    public void requestSenhaIsNull(){
        UsuarioTO usuarioTO = new UsuarioTO(UUID.randomUUID(), "email@gmail.com", null);

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> usuarioService.postLogin(usuarioTO)).isInstanceOf(FailedConditional.class).hasMessage("Obrigatório informar senha.");
        });
    }

    @Test
    @SneakyThrows
    public void requestSenhaIsEmpty(){
        UsuarioTO usuarioTO = new UsuarioTO(UUID.randomUUID(), "email@gmail.com", "");

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> usuarioService.postLogin(usuarioTO)).isInstanceOf(FailedConditional.class).hasMessage("Obrigatório informar senha.");
        });
    }

    @Test
    @SneakyThrows
    public void requestEmailIsNull(){
        UsuarioTO usuarioTO = new UsuarioTO(UUID.randomUUID(), null, "teste123");

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> usuarioService.postLogin(usuarioTO)).isInstanceOf(IsNull.class).hasMessage("Obrigatório informar email.");
        });
    }

    @Test
    @SneakyThrows
    public void requestEmailIsBlank(){
        UsuarioTO usuarioTO = new UsuarioTO(UUID.randomUUID(), "", "teste123");

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> usuarioService.postLogin(usuarioTO)).isInstanceOf(IsNull.class).hasMessage("Obrigatório informar email.");
        });
    }

    @Test
    @SneakyThrows
    public void requestEmailInvalid(){
        UsuarioTO usuarioTO = new UsuarioTO(UUID.randomUUID(), "titiu.com.br", "teste123");

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> usuarioService.postLogin(usuarioTO)).isInstanceOf(FailedConditional.class).hasMessage("E-mail inválido.");
        });
    }

    @Test
    @Transactional
    public void deleteUsuario(){
        usuarioRepository.deleteAll();
        UsuarioTO usuarioSave = usuarioDataProvider.createUsuarioCustom("delete.user@gmail.com", "delete123");
        long afterSave = usuarioRepository.count();

        usuarioService.deleteUsuario(usuarioSave.uuid());
        long afterDeleted = usuarioRepository.count();

        SoftAssertions.assertSoftly(s ->  {
            s.assertThat(afterSave).isEqualTo(1);
            s.assertThat(afterDeleted).isEqualTo(0);
        });
    }

    @Test
    public void deleteUsuarioIsNull(){
        UUID uuidFake = UUID.randomUUID();

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> usuarioService.deleteUsuario(uuidFake)).isInstanceOf(IsNull.class)
                    .hasMessage("Usuário não encontrado.");
        });
    }
}
