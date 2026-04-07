package com.mfinancas.api.categoria;

import com.mfinancas.api.TipoCategoria;
import com.mfinancas.api.dataprovider.CategoriaDataProvider;
import com.mfinancas.api.dataprovider.UsuarioDataProvider;
import com.mfinancas.api.api.dto.categoria.CategoriaDTO;
import com.mfinancas.api.api.dto.usuario.UsuarioDTO;
import com.mfinancas.api.repository.categoria.CategoriaRepository;
import com.mfinancas.api.service.categoria.CategoriaService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class CategoriaRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private CategoriaDataProvider categoriaDataProvider;

    @Autowired
    private UsuarioDataProvider usuarioDataProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getCategorias() throws Exception {
        categoriaRepository.deleteAll();
        UsuarioDTO usuario = usuarioDataProvider.createUsuarioTO();
        CategoriaDTO categoriaDTO1 = new CategoriaDTO(UUID.randomUUID(), "Teste1", TipoCategoria.DESPESA, usuario.uuid());
        categoriaService.createCategoria(categoriaDTO1, categoriaDTO1.usuarioFK());

        CategoriaDTO categoriaDTO2 = new CategoriaDTO(UUID.randomUUID(), "Teste2", TipoCategoria.DESPESA, usuario.uuid());
        categoriaService.createCategoria(categoriaDTO2, categoriaDTO2.usuarioFK());

        mockMvc.perform(get("/categoria").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$", hasSize(2)))

                .andExpect(jsonPath("$[*].nome", containsInAnyOrder("Teste1", "Teste2")));
    }

    @Test
    public void findAllCategoriasResponseVazio() throws Exception {
        categoriaRepository.deleteAll();

        mockMvc.perform(get("/categoria")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    public void postCategoria() throws Exception {
        categoriaRepository.deleteAll();
        UsuarioDTO usuario = usuarioDataProvider.createUsuarioTO();
        CategoriaDTO categoriaDTO = new CategoriaDTO(UUID.randomUUID(), "Teste1", TipoCategoria.DESPESA, usuario.uuid());

        String categoriaRequest = objectMapper.writeValueAsString(categoriaDTO);

        mockMvc.perform(post("/categoria/create/" + usuario.uuid()).contentType(MediaType.APPLICATION_JSON).content(categoriaRequest).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Teste1"))
                .andExpect(jsonPath("$.tipo").value("DESPESA"))
                .andExpect(jsonPath("$.usuarioFK").value(usuario.uuid().toString()));
    }

    @Test
    public void putCategoria() throws Exception {
        categoriaRepository.deleteAll();
        CategoriaDTO categoriaSaved = categoriaDataProvider.createCategoria("qualquerCoisa");
        CategoriaDTO categoriaDTO = new CategoriaDTO(categoriaSaved.uuidCategoria(), "Teste1", TipoCategoria.RECEITA, categoriaSaved.usuarioFK());

        String categoriaJSON = objectMapper.writeValueAsString(categoriaDTO);

        mockMvc.perform(put("/categoria/edit/" + categoriaSaved.uuidCategoria()).contentType(MediaType.APPLICATION_JSON).content(categoriaJSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Teste1"))
                .andExpect(jsonPath("$.tipo").value("RECEITA"))
                .andExpect(jsonPath("$.usuarioFK").value(categoriaSaved.usuarioFK().toString()));
    }

    @Test
    public void deleteCategoria() throws Exception {
        categoriaRepository.deleteAll();
        CategoriaDTO categoriaCriada = categoriaDataProvider.createCategoria("testeDelete");

        mockMvc.perform(delete("/categoria/delete/" + categoriaCriada.uuidCategoria()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Deletado com sucesso."));
    }

    @Test
    public void categoriaSameName() throws Exception {
        categoriaRepository.deleteAll();

        UsuarioDTO usuario = usuarioDataProvider.createUsuarioTO();

        CategoriaDTO categoria1 = new CategoriaDTO(
                null, "Caminhão", TipoCategoria.DESPESA, usuario.uuid()
        );

        CategoriaDTO categoria2 = new CategoriaDTO(
                null, "Caminhão", TipoCategoria.DESPESA, usuario.uuid()
        );

        String json1 = objectMapper.writeValueAsString(categoria1);
        String json2 = objectMapper.writeValueAsString(categoria2);

        mockMvc.perform(post("/categoria/create/" + usuario.uuid()).contentType(MediaType.APPLICATION_JSON).content(json1))
                .andExpect(status().isOk());

        mockMvc.perform(post("/categoria/create/" + usuario.uuid()).contentType(MediaType.APPLICATION_JSON).content(json2))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Já existe um nome cadastrado com essa categoria."));
    }

    @Test
    public void categoriaNomeVazio() throws Exception {
        UsuarioDTO usuario = usuarioDataProvider.createUsuarioTO();

        CategoriaDTO categoriaDTO = new CategoriaDTO(
                UUID.randomUUID(), "", TipoCategoria.DESPESA, usuario.uuid()
        );

        String json = objectMapper.writeValueAsString(categoriaDTO);

        mockMvc.perform(post("/categoria/create/" + usuario.uuid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Obrigatório informar o descricao da categoria."));
    }

    @Test
    public void categoriaTipoNull() throws Exception {
        UsuarioDTO usuario = usuarioDataProvider.createUsuarioTO();

        CategoriaDTO categoriaDTO = new CategoriaDTO(
                UUID.randomUUID(), "Grama", null, usuario.uuid()
        );

        String json = objectMapper.writeValueAsString(categoriaDTO);

        mockMvc.perform(post("/categoria/create/" + usuario.uuid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Obrigatório informar o  tipo da categoria."));
    }
}
