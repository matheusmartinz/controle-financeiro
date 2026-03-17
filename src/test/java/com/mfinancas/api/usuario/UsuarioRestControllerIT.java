package com.mfinancas.api.usuario;

import com.mfinancas.api.dataprovider.UsuarioDataProvider;
import com.mfinancas.api.dto.UsuarioDTO;
import com.mfinancas.api.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class UsuarioRestControllerIT {

    @Autowired
    private UsuarioDataProvider usuarioDataProvider;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void notFoundUsuario() throws Exception {
        usuarioRepository.deleteAll();
        String usuarioJSON = objectMapper.writeValueAsString(usuarioDataProvider.createUsuarioTO());

        mockMvc.perform(post("/controle-financeiro/cadastre").contentType(MediaType.APPLICATION_JSON).content(usuarioJSON).accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isNotFound());
    }

    @Test
    public void postCadastroUsuario() throws Exception {
        usuarioRepository.deleteAll();
        String usuarioJSON = objectMapper.writeValueAsString(usuarioDataProvider.createUsuarioTO());

        mockMvc.perform(post("/controle-financeiro/cadastro").contentType(MediaType.APPLICATION_JSON).content(usuarioJSON).accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("jorge@gmail.com"))
                .andExpect(jsonPath("$.senha").value("SEGREDO"));

    }

    @Test
    public void getAllUsuarios() throws Exception {
        usuarioRepository.deleteAll();
        long before = usuarioRepository.count();
        usuarioDataProvider.createUsuarioCustom("ribeiro@gmail.com", "ribeiro123");
        usuarioDataProvider.createUsuarioCustom("pedro@gmail.com", "pedro123");
        long after = usuarioRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(after).isEqualTo(before + 2);
        });

        mockMvc.perform(get("/controle-financeiro").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    public void postLogin() throws Exception{
        usuarioRepository.deleteAll();
        usuarioDataProvider.createUsuarioCustom("testeLogin@gmail.com", "teste123");

        UsuarioDTO usuarioRequest = new UsuarioDTO(UUID.randomUUID(), "testeLogin@gmail.com", "teste123");

        String usuarioJSON = objectMapper.writeValueAsString(usuarioRequest);

        mockMvc.perform(post("/controle-financeiro/login").contentType(MediaType.APPLICATION_JSON).content(usuarioJSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("testeLogin@gmail.com"))
                .andExpect(jsonPath("$.senha").value("SEGREDO"));
    }

    @Test
    public void postLoginError() throws Exception{
        usuarioRepository.deleteAll();
        usuarioDataProvider.createUsuarioCustom("testeLogin@gmail.com", "teste123");

        UsuarioDTO usuarioRequest = new UsuarioDTO(UUID.randomUUID(), "testeError@gmail.com", "teste123");

        String usuarioJSON = objectMapper.writeValueAsString(usuarioRequest);

        mockMvc.perform(post("/controle-financeiro/login").contentType(MediaType.APPLICATION_JSON).content(usuarioJSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Credenciais incorretos."));
    }

    @Test
    public void deleteUsuario() throws Exception{
        usuarioRepository.deleteAll();
        UsuarioDTO usuarioSave = usuarioDataProvider.createUsuarioCustom("deleteUsu@gmail.com", "delete123");

        mockMvc.perform(delete("/controle-financeiro/delete-usuario/" + usuarioSave.uuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Usuário deletado com sucesso."));
    }
}
