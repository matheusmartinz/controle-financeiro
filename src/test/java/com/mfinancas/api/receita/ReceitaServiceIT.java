package com.mfinancas.api.receita;

import com.mfinancas.api.dataprovider.CategoriaDataProvider;
import com.mfinancas.api.dataprovider.ReceitaDataProvider;
import com.mfinancas.api.dataprovider.UsuarioDataProvider;
import com.mfinancas.api.api.dto.categoria.CategoriaDTO;
import com.mfinancas.api.api.dto.receita.ReceitaDTO;
import com.mfinancas.api.api.dto.usuario.UsuarioDTO;
import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.domain.entity.receita.Receita;
import com.mfinancas.api.repository.receita.ReceitaRepository;
import com.mfinancas.api.repository.usuario.UsuarioRepository;
import com.mfinancas.api.service.receita.ReceitaService;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
public class ReceitaServiceIT {

    @Autowired
    private ReceitaRepository receitaRepository;


    @Autowired
    private ReceitaService receitaService;

    @Autowired
    private ReceitaDataProvider receitaDataProvider;

    @Autowired
    private CategoriaDataProvider categoriaDataProvider;

    @Autowired
    private UsuarioDataProvider usuarioDataProvider;


    @Test
    public void createReceita() {
        long before = receitaRepository.count();
        ReceitaDTO toReturnReceita = receitaDataProvider.createReceitaDataProvider();
        long after = receitaRepository.count();

        Receita receita = receitaRepository.findByUuid(toReturnReceita.uuidReceita());

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(after).isEqualTo(before + 1);
            s.assertThat(toReturnReceita.valor()).isEqualTo(BigDecimal.valueOf(500));
            s.assertThat(toReturnReceita.data()).isNotEqualTo(LocalDate.now().isBefore(LocalDate.now()));
            s.assertThat(toReturnReceita.descricao()).isEqualTo(receita.getDescricao());
        });
    }

    @Test
    @SneakyThrows
    public void receitaUserIsNull() {
        CategoriaDTO categoriaDTO = categoriaDataProvider.createCategoria("Extra");
        ReceitaDTO receita = new ReceitaDTO(UUID.randomUUID(), "Freela", BigDecimal.valueOf(400), LocalDate.now(), categoriaDTO.uuidCategoria(), null);

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> receitaService.createReceita(receita)).isInstanceOf(IsNull.class)
                    .hasMessage("Usuário não encontrado.");
        });
    }

    @Test
    @SneakyThrows
    public void receitaCategoriaIsNull() {
        UsuarioDTO usuarioDTO = usuarioDataProvider.createUsuarioTO();
        ReceitaDTO receita = new ReceitaDTO(UUID.randomUUID(), "Freela", BigDecimal.valueOf(400), LocalDate.now(), null, usuarioDTO.uuid());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> receitaService.createReceita(receita)).isInstanceOf(IsNull.class)
                    .hasMessage("Categoria não encontrada.");
        });
    }

    @Test
    public void getReceita() {
        receitaRepository.deleteAll();
        long before = receitaRepository.count();
        receitaDataProvider.createReceitaCustom("Uber de quinta", BigDecimal.valueOf(400), LocalDate.now(), "Dinheirin");
        ReceitaDTO toReturnReceita2 = receitaDataProvider.createReceitaCustom("Marido de aluguel", BigDecimal.valueOf(500), LocalDate.now().minusDays(2), "extra");
        long after = receitaRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(after).isEqualTo(before + 2);
            s.assertThat(toReturnReceita2.data()).isBefore(LocalDate.now());
        });
    }

    @Test
    public void updateReceita(){
        receitaRepository.deleteAll();
        long before = receitaRepository.count();
        ReceitaDTO receita = receitaDataProvider.createReceitaCustom("Uber de quinta", BigDecimal.valueOf(400), LocalDate.now().minusDays(1), "Dinheirin3");
        long after = receitaRepository.count();

        Receita receitaSaved = receitaRepository.findByUuid(receita.uuidReceita());

        ReceitaDTO receitaDTO = new ReceitaDTO(receitaSaved.getUuid(), "Garcom de sexta", BigDecimal.valueOf(100), LocalDate.now(), receitaSaved.getCategoriaFK(), receitaSaved.getUsuarioFK());

        ReceitaDTO receitaUpdated = receitaService.updateReceita(receitaDTO, receita.uuidReceita());

        Receita receitaSavedUpdated = receitaRepository.findByUuid(receitaUpdated.uuidReceita());
        long afterUpdate = receitaRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(after).isEqualTo(before + 1);
            s.assertThat(afterUpdate).isEqualTo(after);
            s.assertThat(receitaSavedUpdated.getUuid()).isEqualTo(receitaSaved.getUuid());

            s.assertThat(receitaSavedUpdated.getData()).isNotEqualTo(receitaSaved);
        });
    }

    @Test
    public void updateReceitaIsNull(){
        receitaRepository.deleteAll();
        ReceitaDTO receita = receitaDataProvider.createReceitaCustom("Uber de quinta", BigDecimal.valueOf(400), LocalDate.now().minusDays(1), "Dinheirin2");

        ReceitaDTO receitaDTO = new ReceitaDTO(UUID.randomUUID(), "Garcom de sexta", BigDecimal.valueOf(100), LocalDate.now(), receita.categoriaFK(), receita.usuarioFK());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> receitaService.updateReceita(receitaDTO, receitaDTO.uuidReceita())).isInstanceOf(IsNull.class)
                    .hasMessage("Receita não encontrada.");
        });
    }

    @Test
    @Transactional
    public void deleteReceita(){
        receitaRepository.deleteAll();
        ReceitaDTO receitaSalva = receitaDataProvider.createReceitaCustom("Piloto de Avião", BigDecimal.valueOf(1500), LocalDate.now(), "Dinheiro da pinga");
        long afterSave = receitaRepository.count();

        receitaService.deleteReceita(receitaSalva.uuidReceita());
        long afterDelete = receitaRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(afterSave).isEqualTo(1);
            s.assertThat(afterDelete).isEqualTo(0);
        });
    }

    @Test
    public void deleteReceitaIsNull(){
       SoftAssertions.assertSoftly(s -> {
           s.assertThatThrownBy(() -> receitaService.deleteReceita(UUID.randomUUID())).isInstanceOf(IsNull.class)
                   .hasMessage("Receita não encontrada.");
       });
    }
}
