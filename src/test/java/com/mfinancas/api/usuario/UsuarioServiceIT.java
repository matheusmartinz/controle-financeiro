package com.mfinancas.api.usuario;

import com.mfinancas.api.api.dto.usuario.UsuarioDTO;
import com.mfinancas.api.dataprovider.UsuarioDataProvider;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.repository.usuario.UsuarioRepository;
import com.mfinancas.api.service.usuario.UsuarioService;
import jakarta.transaction.Transactional;
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
        UsuarioDTO usuarioDTO = new UsuarioDTO(UUID.randomUUID(), "Matheus", "matheus@gmail.com", "blablabla");
        int before = usuarioRepository.findAll().size();

        UsuarioDTO usuarioResponse = usuarioService.createUsuario(usuarioDTO);

        int after = usuarioRepository.findAll().size();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(usuarioResponse.senha()).isNotEqualTo(null);
            s.assertThat(after).isEqualTo(before + 1);
        });
    }

    @Test
    public void getAllUsuarios() {
        UsuarioDTO usuarioDTO = new UsuarioDTO(UUID.randomUUID(), "Matheus", "matheus@gmail.com", "blablabla");
        usuarioService.createUsuario(usuarioDTO);

        List<UsuarioDTO> listUsuarios = usuarioService.getAll();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(listUsuarios).isNotNull();
            s.assertThat(listUsuarios.size()).isGreaterThan(0);
        });
    }

    @Test
    @Transactional
    public void deleteUsuario() {
        usuarioRepository.deleteAll();
        UsuarioDTO usuarioSave = usuarioDataProvider.createUsuarioCustom("Delete", "delete.user@gmail.com", "delete123");
        long afterSave = usuarioRepository.count();

        usuarioService.deleteUsuario(usuarioSave.uuid());
        long afterDeleted = usuarioRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(afterSave).isEqualTo(1);
            s.assertThat(afterDeleted).isEqualTo(0);
        });
    }

    @Test
    public void deleteUsuarioIsNull() {
        UUID uuidFake = UUID.randomUUID();

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> usuarioService.deleteUsuario(uuidFake)).isInstanceOf(IsNull.class)
                    .hasMessage("Usuário não encontrado.");
        });
    }
}
