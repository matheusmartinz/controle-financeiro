package com.mfinancas.api.categoria;

import com.mfinancas.api.TipoCategoria;
import com.mfinancas.api.dataprovider.UsuarioDataProvider;
import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.service.CategoriaRepository;
import com.mfinancas.api.service.CategoriaService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private UsuarioDataProvider usuarioDataProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getCategorias() throws Exception {
        categoriaRepository.deleteAll();
        UsuarioTO usuario = usuarioDataProvider.createUsuarioTO();
        CategoriaTO categoriaTO1 = new CategoriaTO(UUID.randomUUID(), "Teste1", TipoCategoria.DESPESA, usuario.uuid());
        categoriaService.createCategoria(categoriaTO1, categoriaTO1.usuarioFK());

        CategoriaTO categoriaTO2 = new CategoriaTO(UUID.randomUUID(), "Teste2", TipoCategoria.DESPESA, usuario.uuid());
        categoriaService.createCategoria(categoriaTO2, categoriaTO2.usuarioFK());

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

//    @Test
//    public void postCategoria() throws Exception {
//        categoriaRepository.deleteAll();
//        UsuarioTO usuario = usuarioDataProvider.createUsuarioTO();
//        CategoriaTO categoriaTO = new CategoriaTO(UUID.randomUUID(), "Teste1", TipoCategoria.DESPESA, usuario.uuid());
//
//        String categoriaRequest = objectMapper.writeValueAsString(categoriaTO);
//
//        mockMvc.perform(post("/categoria/create/" + usuario.uuid()).contentType(MediaType.APPLICATION_JSON).content(categoriaRequest).accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.")) VALIDAR O POST
//    }
}
