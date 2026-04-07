package com.mfinancas.api.despesa;

import com.mfinancas.api.api.dto.categoria.CategoriaDTO;
import com.mfinancas.api.dataprovider.CategoriaDataProvider;
import com.mfinancas.api.dataprovider.DespesaDataProvider;
import com.mfinancas.api.api.dto.despesa.DespesaDTO;
import com.mfinancas.api.repository.categoria.CategoriaRepository;
import com.mfinancas.api.repository.despesa.DespesaRepository;
import com.mfinancas.api.service.despesa.DespesaService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    @Autowired
    private CategoriaDataProvider categoriaDataProvider;

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
        DespesaDTO despesaResponse = despesaDataProvider.createDespesaCustom("Ford", "Troca de farol");

        String despesaJson = objectMapper.writeValueAsString(despesaResponse);

        mockMvc.perform(post("/despesa/cadastro").contentType(MediaType.APPLICATION_JSON).content(despesaJson).accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.descricao").value("Troca de farol"))
                .andExpect(jsonPath("$.categoriaFK").value(despesaResponse.categoriaFK().toString()));

        despesaDataProvider.createDespesaCustom("Toyota", "Troca de correia");

    }

    @Test
    public void putDespesa() throws Exception {
        despesaRepository.deleteAll();

        long before = despesaRepository.count();
        DespesaDTO despesaResponse = despesaDataProvider.createDespesaCustom("Filha", "Balé");
        long after = despesaRepository.count();

        DespesaDTO despesaUpdate = new DespesaDTO(despesaResponse.uuidDespesa(), "Pneu", BigDecimal.valueOf(500),
                LocalDate.now().plusDays(20),false, despesaResponse.categoriaFK(), despesaResponse.usuarioFK());

        long afterUpdate = despesaRepository.count();

        String despesaUpdateJSON = objectMapper.writeValueAsString(despesaUpdate);

        mockMvc.perform(put("/despesa/editar/" + despesaUpdate.uuidDespesa()).contentType(MediaType.APPLICATION_JSON)
                        .content(despesaUpdateJSON).accept(MediaType.APPLICATION_JSON))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.categoriaFK").value(despesaUpdate.categoriaFK().toString()))
                .andExpect(jsonPath("$.descricao").value("Pneu"))
                .andExpect(jsonPath("$.uuidDespesa").value(despesaUpdate.uuidDespesa().toString()))
                .andExpect(jsonPath("$.dataVencimento").value(LocalDate.now().plusDays(20).toString()));

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(after).isEqualTo(before + 1);
            s.assertThat(afterUpdate).isEqualTo(after);
        });
    }

    @Test
    public void deleteDespesa() throws Exception{
        despesaRepository.deleteAll();
        DespesaDTO despesaCriadaSalva = despesaDataProvider.createDespesaCustom("testeDeletin", "despesaDeletada");

        mockMvc.perform(delete("/despesa/delete/" + despesaCriadaSalva.uuidDespesa()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value("Deletado com sucesso."));
    }

    @Test
    public void createDespesaDescricaoNull() throws Exception {
        despesaRepository.deleteAll();
        CategoriaDTO categoriaDTO = categoriaDataProvider.createCategoria("Carro");

        DespesaDTO despesaDTO = new DespesaDTO(
                UUID.randomUUID(), null, BigDecimal.valueOf(450), LocalDate.now(),
                false, categoriaDTO.uuidCategoria(), categoriaDTO.usuarioFK()
        );

        String despesaJSON = objectMapper.writeValueAsString(despesaDTO);

        mockMvc.perform(post("/despesa/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(despesaJSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Obrigatório informar a descrição."));
    }

    @Test
    public void createDespesaDescricaoIsEmpty() throws Exception {
        CategoriaDTO categoriaDTO = categoriaDataProvider.createCategoria("Carro2");

        DespesaDTO despesaDTO = new DespesaDTO(
                UUID.randomUUID(), "", BigDecimal.valueOf(450), LocalDate.now(),
                false, categoriaDTO.uuidCategoria(), categoriaDTO.usuarioFK()
        );

        String despesaJSON = objectMapper.writeValueAsString(despesaDTO);

        mockMvc.perform(post("/despesa/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(despesaJSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Obrigatório informar a descrição."));
    }

    @Test
    public void createDespesaValorInvalid() throws Exception {
        CategoriaDTO categoriaDTO = categoriaDataProvider.createCategoria("Alimentação");

        DespesaDTO despesaDTO = new DespesaDTO(
                UUID.randomUUID(), "Ifood", BigDecimal.valueOf(-10), LocalDate.now(),
                false, categoriaDTO.uuidCategoria(), categoriaDTO.usuarioFK()
        );

        String despesaJSON = objectMapper.writeValueAsString(despesaDTO);

        mockMvc.perform(post("/despesa/cadastro")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(despesaJSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("O valor não pode ser menor do que zero."));
    }

}
