package com.mfinancas.api.receita;

import com.mfinancas.api.dataprovider.ReceitaDataProvider;
import com.mfinancas.api.dto.ReceitaDTO;
import com.mfinancas.api.model.Receita;
import com.mfinancas.api.repository.ReceitaRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    @Test
    public void postReceita() throws Exception{
        ReceitaDTO receitaDTO = receitaDataProvider.createReceitaCustom("Uber", BigDecimal.valueOf(900), LocalDate.now(), "Extrinha");
        String receitaJSON = objectMapper.writeValueAsString(receitaDTO);

        mockMvc.perform(post("/receita/create").contentType(MediaType.APPLICATION_JSON).content(receitaJSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.descricao").value("Uber"))
                .andExpect(jsonPath("$.data").value(LocalDate.now().toString()));
    }

    @Test
    public void getReceita() throws Exception{
        receitaRepository.deleteAll();
        receitaDataProvider.createReceitaCustom("Uber", BigDecimal.valueOf(900), LocalDate.now(), "Extrinh2");
        receitaDataProvider.createReceitaCustom("Uber", BigDecimal.valueOf(900), LocalDate.now(), "Extrinha3");

        mockMvc.perform(get("/receita").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$",hasSize(2)));
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
}
