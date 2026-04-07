package com.mfinancas.api.usuario;

import com.mfinancas.api.dataprovider.UsuarioDataProvider;
import com.mfinancas.api.api.dto.usuario.UsuarioDTO;
import com.mfinancas.api.repository.usuario.UsuarioRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        String usuarioJSON = objectMapper.writeValueAsString(new UsuarioDTO(UUID.randomUUID(),"Cleiton", "cleiton.jose@gmail.com", "clieton123"));

        mockMvc.perform(post("/controle-financeiro/cadastro").contentType(MediaType.APPLICATION_JSON).content(usuarioJSON).accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("cleiton.jose@gmail.com"))
                .andExpect(jsonPath("$.senha").value(""));

    }

    @Test
    public void getAllUsuarios() throws Exception {
        usuarioRepository.deleteAll();
        long before = usuarioRepository.count();
        usuarioDataProvider.createUsuarioCustom("Ribeiro", "ribeiro@gmail.com", "ribeiro123");
        usuarioDataProvider.createUsuarioCustom("Pedro Correia", "pedro@gmail.com", "pedro123");
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
    public void postLogin() throws Exception {
        usuarioRepository.deleteAll();
        usuarioDataProvider.createUsuarioCustom("Pedro Correia", "testeLogin@gmail.com", "teste123");

        UsuarioDTO usuarioRequest = new UsuarioDTO(UUID.randomUUID(), "Pedro Correia", "testeLogin@gmail.com", "teste123");

        String usuarioJSON = objectMapper.writeValueAsString(usuarioRequest);

        mockMvc.perform(post("/controle-financeiro/login").contentType(MediaType.APPLICATION_JSON).content(usuarioJSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email").value("testeLogin@gmail.com"))
                .andExpect(jsonPath("$.senha").value(""));
    }

    @Test
    public void postSenhaIsNull() throws Exception {
        usuarioRepository.deleteAll();
        usuarioDataProvider.createUsuarioCustom("Pedro Correia", "testeLogin@gmail.com", "teste123");

        UsuarioDTO usuarioRequest = new UsuarioDTO(UUID.randomUUID(), "Pedro Correia", "testeLogin@gmail.com", null);

        String usuarioJSON = objectMapper.writeValueAsString(usuarioRequest);

        mockMvc.perform(post("/controle-financeiro/login").contentType(MediaType.APPLICATION_JSON).content(usuarioJSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Obrigatório informar senha."));
    }

    @Test
    public void postSenhaIsEmpty() throws Exception {
        usuarioRepository.deleteAll();
        usuarioDataProvider.createUsuarioCustom("Matheus", "email@gmail.com", "teste123");

        UsuarioDTO usuarioRequest = new UsuarioDTO(UUID.randomUUID(), "Matheus", "email@gmail.com", "");

        String usuarioJSON = objectMapper.writeValueAsString(usuarioRequest);

        mockMvc.perform(post("/controle-financeiro/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Obrigatório informar senha."));
    }

    @Test
    public void postEmailInvalid() throws Exception {
        UsuarioDTO usuarioRequest = new UsuarioDTO(UUID.randomUUID(), "Matheus", "titiu.com.br", "teste123");

        String usuarioJSON = objectMapper.writeValueAsString(usuarioRequest);

        mockMvc.perform(post("/controle-financeiro/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(usuarioJSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("E-mail inválido."));
    }

    @Test
    public void postLoginError() throws Exception {
        usuarioRepository.deleteAll();
        usuarioDataProvider.createUsuarioCustom("Pedro Correia", "testeLogin@gmail.com", "teste123");

        UsuarioDTO usuarioRequest = new UsuarioDTO(UUID.randomUUID(), "Pedro Correia", "testeError@gmail.com", "teste123");

        String usuarioJSON = objectMapper.writeValueAsString(usuarioRequest);

        mockMvc.perform(post("/controle-financeiro/login").contentType(MediaType.APPLICATION_JSON).content(usuarioJSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Credenciais incorretos."));
    }

    @Test
    public void deleteUsuario() throws Exception {
        usuarioRepository.deleteAll();
        UsuarioDTO usuarioSave = usuarioDataProvider.createUsuarioCustom("Pedro Correia", "deleteUsu@gmail.com", "delete123");

        mockMvc.perform(delete("/controle-financeiro/delete-usuario/" + usuarioSave.uuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Usuário deletado com sucesso."));
    }
}
