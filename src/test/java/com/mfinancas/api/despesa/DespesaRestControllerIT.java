package com.mfinancas.api.despesa;

import com.mfinancas.api.dataprovider.DespesaDataProvider;
import com.mfinancas.api.dto.DespesaTO;
import com.mfinancas.api.repository.DespesaRepository;
import com.mfinancas.api.service.DespesaService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DespesaRestControllerIT {
    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DespesaDataProvider despesaDataProvider;

    @Autowired
    private DespesaService despesaService;

    @Test
    public void notFoundReturn() throws Exception {
        mockMvc.perform(get("/dis").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getDespesas() throws Exception {
        despesaRepository.deleteAll();

        despesaDataProvider.createDespesaCustom("Porche", "Troca de óleo");
        despesaDataProvider.createDespesaCustom("Camaro", "Troca de pneu");

        despesaService.findAllDespesas();

        mockMvc.perform(get("/despesa").accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))

                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].descricao", containsInAnyOrder("Troca de pneu", "Troca de óleo")));
    }

    @Test
    public void postDespesa() throws Exception {
        despesaRepository.deleteAll();
        DespesaTO despesaResponse = despesaDataProvider.createDespesaCustom("Ford", "Troca de farol");

        String despesaJson = objectMapper.writeValueAsString(despesaResponse);

        mockMvc.perform(post("/despesa/cadastro").contentType(MediaType.APPLICATION_JSON).content(despesaJson).accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.descricao").value("Troca de farol"))
                .andExpect(jsonPath("$.categoriaFK").value(despesaResponse.categoriaFK().toString()));

        despesaDataProvider.createDespesaCustom("Toyota", "Troca de correia");

    }

}
