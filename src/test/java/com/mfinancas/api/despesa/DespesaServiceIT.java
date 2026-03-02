package com.mfinancas.api.despesa;

import com.mfinancas.api.dataprovider.CategoriaDataProvider;
import com.mfinancas.api.dataprovider.DespesaDataProvider;
import com.mfinancas.api.dataprovider.UsuarioDataProvider;
import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.dto.DespesaTO;
import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.model.Despesa;
import com.mfinancas.api.repository.DespesaRepository;
import com.mfinancas.api.service.DespesaService;
import jakarta.transaction.Transactional;
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
public class DespesaServiceIT {
    @Autowired
    private DespesaRepository despesaRepository;

    @Autowired
    private DespesaDataProvider despesaDataProvider;

    @Autowired
    private CategoriaDataProvider categoriaDataProvider;

    @Autowired
    private DespesaService despesaService;

    @Autowired
    private UsuarioDataProvider usuarioDataProvider;

    @Test
    public void createDespesa() {
        long before = despesaRepository.count();

        DespesaTO despesaCreated = despesaDataProvider.createDespesa();

        long after = despesaRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(after).isEqualTo(before + 1);
            s.assertThat(despesaCreated).isNotNull();
            s.assertThat(despesaCreated.pago()).isEqualTo(false);
            s.assertThat(despesaCreated.valor()).isGreaterThan(BigDecimal.ZERO);
        });
    }

    @Test
    public void createDespesaUsuarioIsNull() {

        CategoriaTO categoriaTO = categoriaDataProvider.createCategoria("Mercado");

        DespesaTO despesaTO = new DespesaTO(UUID.randomUUID(), "Compra do mes", BigDecimal.valueOf(1.200), LocalDate.now(),
                true, categoriaTO.uuidCategoria(), null);

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> despesaService.createDespesa(despesaTO))
                    .isInstanceOf(IsNull.class)
                    .hasMessage("Usuário não encontrado.");
        });
    }

    @Test
    public void createDespesaDescricaoNull() {
        CategoriaTO categoriaTO = categoriaDataProvider.createCategoria("Carro");

        DespesaTO despesaTO = new DespesaTO(UUID.randomUUID(), null, BigDecimal.valueOf(450), LocalDate.now(),
                false, categoriaTO.uuidCategoria(), categoriaTO.usuarioFK());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> despesaService.createDespesa(despesaTO))
                    .isInstanceOf(FailedConditional.class)
                    .hasMessage("Favor informar a descrição.");
        });
    }

    @Test
    public void createDespesaDescricaoIsEmpty() {
        CategoriaTO categoriaTO = categoriaDataProvider.createCategoria("Carro2");

        DespesaTO despesaTO = new DespesaTO(UUID.randomUUID(), "", BigDecimal.valueOf(450), LocalDate.now(),
                false, categoriaTO.uuidCategoria(), categoriaTO.usuarioFK());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> despesaService.createDespesa(despesaTO))
                    .isInstanceOf(FailedConditional.class)
                    .hasMessage("Favor informar a descrição.");
        });
    }

    @Test
    public void createDespesaValorInvalid() {
        CategoriaTO categoriaTO = categoriaDataProvider.createCategoria("Alimentação");

        DespesaTO despesaTO = new DespesaTO(UUID.randomUUID(), "Ifood", BigDecimal.valueOf(-10), LocalDate.now(),
                false, categoriaTO.uuidCategoria(), categoriaTO.usuarioFK());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> despesaService.createDespesa(despesaTO))
                    .isInstanceOf(FailedConditional.class)
                    .hasMessage("O valor deve ser maior do que zero.");
        });
    }

    @Test
    public void createDespesaCategoriaFKNull() {
        CategoriaTO categoriaTO = categoriaDataProvider.createCategoria("Viagem");

        DespesaTO despesaTO = new DespesaTO(UUID.randomUUID(), "Resort nordeste", BigDecimal.valueOf(4000), LocalDate.now(),
                false, UUID.randomUUID(), categoriaTO.usuarioFK());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> despesaService.createDespesa(despesaTO))
                    .isInstanceOf(IsNull.class)
                    .hasMessage("Categoria não encontrada.");
        });
    }

    @Test
    public void despesaIsPago() {
        CategoriaTO categoriaTO = categoriaDataProvider.createCategoria("Isso2");

        DespesaTO despesaTO = new DespesaTO(UUID.randomUUID(), "Um milhao", BigDecimal.valueOf(4000), LocalDate.now().minusDays(1),
                true, categoriaTO.uuidCategoria(), categoriaTO.usuarioFK());

        Despesa despesa = new Despesa(despesaTO);

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(despesa.isPago()).isEqualTo(true);
        });
    }

    @Test
    public void despesaIsAtrasado() {
        CategoriaTO categoriaTO = categoriaDataProvider.createCategoria("Familia");

        DespesaTO despesaTO = new DespesaTO(UUID.randomUUID(), "Presente Natal", BigDecimal.valueOf(4000), LocalDate.now().minusDays(1),
                false, categoriaTO.uuidCategoria(), categoriaTO.usuarioFK());

        Despesa despesa = new Despesa(despesaTO);

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(despesa.isAtrasado()).isEqualTo(true);
        });
    }

    @Test
    public void despesaIsEmDia() {
        CategoriaTO categoriaTO = categoriaDataProvider.createCategoria("QualCoisa");

        DespesaTO despesaTO = new DespesaTO(UUID.randomUUID(), "Isso", BigDecimal.valueOf(4000), LocalDate.now(),
                true, categoriaTO.uuidCategoria(), categoriaTO.usuarioFK());

        Despesa despesa = new Despesa(despesaTO);

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(despesa.isEmDia()).isEqualTo(true);
        });
    }

    @Test
    public void updateDespesa() {
        CategoriaTO categoriaTO = categoriaDataProvider.createCategoria("QualCoisa3");

        DespesaTO despesaTO = new DespesaTO(UUID.randomUUID(), "Isso2", BigDecimal.valueOf(890), LocalDate.now().minusDays(1),
                false, categoriaTO.uuidCategoria(), categoriaTO.usuarioFK());

        Despesa despesa = new Despesa(despesaTO);

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(despesa.isAtrasado()).isEqualTo(true);
            s.assertThat(despesa.getDataVencimento()).isBefore(LocalDate.now());
            s.assertThat(despesa.getPago()).isEqualTo(false);
        });

        DespesaTO toReturnResponse = despesaService.createDespesa(despesaTO);

        long before = despesaRepository.count();

        DespesaTO novaDespesaTO = new DespesaTO(toReturnResponse.uuidDespesa(), "nova despesa", BigDecimal.valueOf(890), LocalDate.now().minusDays(1),
                true, toReturnResponse.categoriaFK(), toReturnResponse.usuarioFK());

        DespesaTO despesaAtualizada = despesaService.updateDespesa(novaDespesaTO, novaDespesaTO.uuidDespesa());

        Despesa entidadeAtualizada = new Despesa(despesaAtualizada);

        long after = despesaRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(after).isEqualTo(before);
            s.assertThat(despesaAtualizada.uuidDespesa()).isEqualTo(toReturnResponse.uuidDespesa());
            s.assertThat(despesaAtualizada.descricao()).isNotEqualTo(toReturnResponse.descricao());
            s.assertThat(despesaAtualizada.dataVencimento()).isEqualTo(toReturnResponse.dataVencimento());
            s.assertThat(despesaAtualizada.pago()).isNotEqualTo(toReturnResponse.pago());
            s.assertThat(despesaAtualizada.categoriaFK()).isEqualTo(toReturnResponse.categoriaFK());
            s.assertThat(despesaAtualizada.usuarioFK()).isEqualTo(toReturnResponse.usuarioFK());
            s.assertThat(entidadeAtualizada.isPago()).isEqualTo(true);
            s.assertThat(entidadeAtualizada.isAtrasado()).isEqualTo(false);
            s.assertThat(entidadeAtualizada.isEmDia()).isEqualTo(false);
        });
    }

    @Test
    @SneakyThrows
    public void updateDespesaIsNull(){
        CategoriaTO categoriaTO = categoriaDataProvider.createCategoria("Outros");
        UsuarioTO usuarioTO = usuarioDataProvider.createUsuarioTO();
        despesaDataProvider.createDespesaCustom("Viajens", "Horlando");

        DespesaTO despesaTO = new DespesaTO(UUID.randomUUID(), "surpresa niver", BigDecimal.valueOf(200), LocalDate.now().plusDays(15),false, categoriaTO.uuidCategoria(), usuarioTO.uuid());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> despesaService.updateDespesa(despesaTO, despesaTO.uuidDespesa())).isInstanceOf(IsNull.class)
                    .hasMessage("Despesa não encontrada.");
        });
    }

    @Test
    @Transactional
    public void deleteDespesa(){
        despesaRepository.deleteAll();
        DespesaTO despesaCreated = despesaDataProvider.createDespesaCustom("Sogra", "Buque");
        long afterCreated = despesaRepository.count();
        despesaService.deleteDespesa(despesaCreated.uuidDespesa());
        long afterDeleted = despesaRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(afterCreated).isEqualTo(1);
            s.assertThat(afterDeleted).isEqualTo(0);
        });
    }

    @Test
    @Transactional
    public void deleteDespesaIsNull(){
        despesaRepository.deleteAll();
        DespesaTO despesaCreated = despesaDataProvider.createDespesaCustom("Sogra2", "Buque");


        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> despesaService.deleteDespesa(null)).isInstanceOf(IsNull.class)
                    .hasMessage("Despesa não encontrada.");
        });
    }
}
