package com.mfinancas.api.receita;

import com.mfinancas.api.dataprovider.CategoriaDataProvider;
import com.mfinancas.api.dataprovider.ReceitaDataProvider;
import com.mfinancas.api.dataprovider.UsuarioDataProvider;
import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.dto.ReceitaTO;
import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.model.Receita;
import com.mfinancas.api.repository.ReceitaRepository;
import com.mfinancas.api.repository.UsuarioRepository;
import com.mfinancas.api.service.ReceitaService;
import lombok.SneakyThrows;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
public class ReceitaServiceIT {

    @Autowired
    private ReceitaRepository receitaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ReceitaService receitaService;

    @Autowired
    private ReceitaDataProvider receitaDataProvider;

    @Autowired
    private UsuarioDataProvider usuarioDataProvider;

    @Autowired
    private CategoriaDataProvider categoriaDataProvider;

    @Test
    public void createReceita() {
        long before = receitaRepository.count();
        ReceitaTO toReturnReceita = receitaDataProvider.createReceitaDataProvider();
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
        CategoriaTO categoriaTO = categoriaDataProvider.createCategoria("Extra");
        ReceitaTO receita = new ReceitaTO(UUID.randomUUID(), "Freela", BigDecimal.valueOf(400), LocalDate.now(), categoriaTO.uuidCategoria(), null);

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> receitaService.createReceita(receita)).isInstanceOf(IsNull.class)
                    .hasMessage("Usuário não encontrado.");
        });
    }

    @Test
    @SneakyThrows
    public void receitaCategoriaIsNull() {
        UsuarioTO usuarioTO = usuarioDataProvider.createUsuarioTO();
        ReceitaTO receita = new ReceitaTO(UUID.randomUUID(), "Freela", BigDecimal.valueOf(400), LocalDate.now(), null, usuarioTO.uuid());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> receitaService.createReceita(receita)).isInstanceOf(IsNull.class)
                    .hasMessage("Categoria não encontrada.");
        });
    }

    @Test
    @SneakyThrows
    public void receitaDescricaoIsNull() {
        UsuarioTO usuarioTO = usuarioDataProvider.createUsuarioTO();
        CategoriaTO categoriaTO = categoriaDataProvider.createCategoria("Uber");
        ReceitaTO receita = new ReceitaTO(UUID.randomUUID(), null, BigDecimal.valueOf(400), LocalDate.now(), categoriaTO.uuidCategoria(), usuarioTO.uuid());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> receitaService.createReceita(receita)).isInstanceOf(FailedConditional.class)
                    .hasMessage("Obrigatório informar a descricão.");
        });
    }

    @Test
    @SneakyThrows
    public void receitaDescricaoIsEmpty() {
        UsuarioTO usuarioTO = usuarioDataProvider.createUsuarioTO();
        CategoriaTO categoriaTO = categoriaDataProvider.createCategoria("Uber4");
        ReceitaTO receita = new ReceitaTO(UUID.randomUUID(), "", BigDecimal.valueOf(400), LocalDate.now(), categoriaTO.uuidCategoria(), usuarioTO.uuid());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> receitaService.createReceita(receita)).isInstanceOf(FailedConditional.class)
                    .hasMessage("Obrigatório informar a descricão.");
        });
    }

    @Test
    @SneakyThrows
    public void receitaValorIsInvalid() {
        UsuarioTO usuarioTO = usuarioDataProvider.createUsuarioTO();
        CategoriaTO categoriaTO = categoriaDataProvider.createCategoria("Uber3");
        ReceitaTO receita = new ReceitaTO(UUID.randomUUID(), "Presente de natal", BigDecimal.valueOf(-100), LocalDate.now(), categoriaTO.uuidCategoria(), usuarioTO.uuid());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> receitaService.createReceita(receita)).isInstanceOf(FailedConditional.class)
                    .hasMessage("O valor deve ser maior que zero.");
        });
    }

    @Test
    @SneakyThrows
    public void receitaDateInvalid() {
        UsuarioTO usuarioTO = usuarioDataProvider.createUsuarioTO();
        CategoriaTO categoriaTO = categoriaDataProvider.createCategoria("Uber2");
        ReceitaTO receita = new ReceitaTO(UUID.randomUUID(), "Extra", BigDecimal.valueOf(500), LocalDate.now().plusDays(10), categoriaTO.uuidCategoria(), usuarioTO.uuid());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> receitaService.createReceita(receita)).isInstanceOf(FailedConditional.class)
                    .hasMessage("Não pode inserir recebimentos futuros.");
        });
    }

    @Test
    public void getReceita() {
        receitaRepository.deleteAll();
        long before = receitaRepository.count();
        receitaDataProvider.createReceitaCustom("Uber de quinta", BigDecimal.valueOf(400), LocalDate.now(), "Dinheirin");
        ReceitaTO toReturnReceita2 = receitaDataProvider.createReceitaCustom("Marido de aluguel", BigDecimal.valueOf(500), LocalDate.now().minusDays(2), "extra");
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
        ReceitaTO receita = receitaDataProvider.createReceitaCustom("Uber de quinta", BigDecimal.valueOf(400), LocalDate.now().minusDays(1), "Dinheirin3");
        long after = receitaRepository.count();

        Receita receitaSaved = receitaRepository.findByUuid(receita.uuidReceita());

        ReceitaTO receitaTO = new ReceitaTO(receitaSaved.getUuid(), "Garcom de sexta", BigDecimal.valueOf(100), LocalDate.now(), receitaSaved.getCategoriaFK(), receitaSaved.getUsuarioFK());

        ReceitaTO receitaUpdated = receitaService.updateReceita(receitaTO, receita.uuidReceita());

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
        ReceitaTO receita = receitaDataProvider.createReceitaCustom("Uber de quinta", BigDecimal.valueOf(400), LocalDate.now().minusDays(1), "Dinheirin2");

        ReceitaTO receitaTO = new ReceitaTO(UUID.randomUUID(), "Garcom de sexta", BigDecimal.valueOf(100), LocalDate.now(), receita.categoriaFK(), receita.usuarioFK());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> receitaService.updateReceita(receitaTO, receitaTO.uuidReceita())).isInstanceOf(IsNull.class)
                    .hasMessage("Receita não encontrada.");
        });
    }
}
