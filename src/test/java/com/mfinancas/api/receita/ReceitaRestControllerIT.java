package com.mfinancas.api.receita;

import com.mfinancas.api.api.dto.categoria.CategoriaDTO;
import com.mfinancas.api.api.dto.receita.ReceitaDTO;
import com.mfinancas.api.api.dto.usuario.UsuarioDTO;
import com.mfinancas.api.dataprovider.CategoriaDataProvider;
import com.mfinancas.api.dataprovider.ReceitaDataProvider;
import com.mfinancas.api.dataprovider.UsuarioDataProvider;
import com.mfinancas.api.domain.entity.receita.Receita;
import com.mfinancas.api.repository.receita.ReceitaRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
public class ReceitaRestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ReceitaDataProvider receitaDataProvider;

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private CategoriaDataProvider categoriaDataProvider;

    @Autowired
    private UsuarioDataProvider usuarioDataProvider;

    @Test
    public void postReceita() throws Exception {
        ReceitaDTO receitaDTO = receitaDataProvider.createReceitaCustom("Uber", BigDecimal.valueOf(900), LocalDate.now(), "Extrinha");
        String receitaJSON = objectMapper.writeValueAsString(receitaDTO);

        mockMvc.perform(post("/receita/create").contentType(MediaType.APPLICATION_JSON).content(receitaJSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.descricao").value("Uber"))
                .andExpect(jsonPath("$.data").value(LocalDate.now().toString()));
    }

    @Test
    public void getReceita() throws Exception {
        receitaRepository.deleteAll();
        receitaDataProvider.createReceitaCustom("Uber", BigDecimal.valueOf(900), LocalDate.now(), "Extrinh2");
        receitaDataProvider.createReceitaCustom("Uber", BigDecimal.valueOf(900), LocalDate.now(), "Extrinha3");

        mockMvc.perform(get("/receita").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void putReceita() throws Exception {
        receitaRepository.deleteAll();
        ReceitaDTO receitaDTO = receitaDataProvider.createReceitaCustom("Uber", BigDecimal.valueOf(900), LocalDate.now(), "Extrinha5");
        Receita receita = receitaRepository.findByUuid(receitaDTO.uuidReceita());

        ReceitaDTO receitaDTOUpdate = new ReceitaDTO(receita.getUuid(), "Garcom de sexta", BigDecimal.valueOf(100), LocalDate.now(), receita.getCategoriaFK(), receita.getUsuarioFK());

        String receitaUpdateJSON = objectMapper.writeValueAsString(receitaDTOUpdate);

        mockMvc.perform(put("/receita/edit/" + receitaDTOUpdate.uuidReceita()).contentType(MediaType.APPLICATION_JSON).content(receitaUpdateJSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Garcom de sexta"))
                .andExpect(jsonPath("$.uuidReceita").value(receita.getUuid().toString()))
                .andExpect(jsonPath("$.categoriaFK").value(receita.getCategoriaFK().toString()));
    }

    @Test
    @Transactional
    public void deleteReceita() throws Exception {
        ReceitaDTO receitaDTO = receitaDataProvider.createReceitaCustom("Uber", BigDecimal.valueOf(900), LocalDate.now(), "Extrinha6");

        mockMvc.perform(delete("/receita/delete/" + receitaDTO.uuidReceita()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Receita deletada com sucesso."));
    }

    @Test
    public void deleteReceitaIsNull() throws Exception {
        UUID uuidFake = UUID.randomUUID();

        mockMvc.perform(delete("/receita/delete/" + uuidFake))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Receita não encontrada."));
    }


    @Test
    public void receitaDescricaoIsNull() throws Exception {
        UsuarioDTO usuarioDTO = usuarioDataProvider.createUsuarioTO();
        CategoriaDTO categoriaDTO = categoriaDataProvider.createCategoria("Uber");

        ReceitaDTO receita = new ReceitaDTO(
                UUID.randomUUID(),
                null,
                BigDecimal.valueOf(400),
                LocalDate.now(),
                categoriaDTO.uuidCategoria(),
                usuarioDTO.uuid()
        );

        mockMvc.perform(post("/receita/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receita)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Obrigatório informar a descrição."));
    }

    @Test
    public void receitaDescricaoIsEmpty() throws Exception {
        UsuarioDTO usuarioDTO = usuarioDataProvider.createUsuarioTO();
        CategoriaDTO categoriaDTO = categoriaDataProvider.createCategoria("Uber4");

        ReceitaDTO receita = new ReceitaDTO(
                UUID.randomUUID(),
                "",
                BigDecimal.valueOf(400),
                LocalDate.now(),
                categoriaDTO.uuidCategoria(),
                usuarioDTO.uuid()
        );

        mockMvc.perform(post("/receita/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receita)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Obrigatório informar a descrição."));
    }

    @Test
    public void receitaValorIsInvalid() throws Exception {
        UsuarioDTO usuarioDTO = usuarioDataProvider.createUsuarioTO();
        CategoriaDTO categoriaDTO = categoriaDataProvider.createCategoria("Uber3");

        ReceitaDTO receita = new ReceitaDTO(
                UUID.randomUUID(),
                "Presente de natal",
                BigDecimal.valueOf(-100),
                LocalDate.now(),
                categoriaDTO.uuidCategoria(),
                usuarioDTO.uuid()
        );

        mockMvc.perform(post("/receita/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receita)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("O valor não pode ser menor do que zero."));
    }

    @Test
    public void receitaDateInvalid() throws Exception {
        UsuarioDTO usuarioDTO = usuarioDataProvider.createUsuarioTO();
        CategoriaDTO categoriaDTO = categoriaDataProvider.createCategoria("Uber2");

        ReceitaDTO receita = new ReceitaDTO(
                UUID.randomUUID(),
                "Extra",
                BigDecimal.valueOf(500),
                LocalDate.now().plusDays(10),
                categoriaDTO.uuidCategoria(),
                usuarioDTO.uuid()
        );

        mockMvc.perform(post("/receita/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(receita)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Não pode inserir recebimentos futuros."));
    }
}
