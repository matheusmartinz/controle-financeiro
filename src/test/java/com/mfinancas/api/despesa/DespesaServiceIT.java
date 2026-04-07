package com.mfinancas.api.despesa;

import com.mfinancas.api.api.dto.categoria.CategoriaDTO;
import com.mfinancas.api.api.dto.despesa.DespesaDTO;
import com.mfinancas.api.api.dto.usuario.UsuarioDTO;
import com.mfinancas.api.dataprovider.CategoriaDataProvider;
import com.mfinancas.api.dataprovider.DespesaDataProvider;
import com.mfinancas.api.dataprovider.UsuarioDataProvider;
import com.mfinancas.api.domain.entity.despesa.Despesa;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.repository.despesa.DespesaRepository;
import com.mfinancas.api.service.despesa.DespesaService;
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

        DespesaDTO despesaCreated = despesaDataProvider.createDespesa();

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

        CategoriaDTO categoriaDTO = categoriaDataProvider.createCategoria("Mercado");

        DespesaDTO despesaDTO = new DespesaDTO(UUID.randomUUID(), "Compra do mes", BigDecimal.valueOf(1.200), LocalDate.now(),
                true, categoriaDTO.uuidCategoria(), null);

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> despesaService.createDespesa(despesaDTO))
                    .isInstanceOf(IsNull.class)
                    .hasMessage("Usuário não encontrado.");
        });
    }

    @Test
    public void createDespesaCategoriaFKNull() {
        CategoriaDTO categoriaDTO = categoriaDataProvider.createCategoria("Viagem");

        DespesaDTO despesaDTO = new DespesaDTO(UUID.randomUUID(), "Resort nordeste", BigDecimal.valueOf(4000), LocalDate.now(),
                false, UUID.randomUUID(), categoriaDTO.usuarioFK());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> despesaService.createDespesa(despesaDTO))
                    .isInstanceOf(IsNull.class)
                    .hasMessage("Categoria não encontrada.");
        });
    }

    @Test
    public void despesaIsPago() {
        CategoriaDTO categoriaDTO = categoriaDataProvider.createCategoria("Isso2");

        DespesaDTO despesaDTO = new DespesaDTO(UUID.randomUUID(), "Um milhao", BigDecimal.valueOf(4000), LocalDate.now().minusDays(1),
                true, categoriaDTO.uuidCategoria(), categoriaDTO.usuarioFK());

        Despesa despesa = new Despesa(despesaDTO);

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(despesa.isPago()).isEqualTo(true);
        });
    }

    @Test
    public void despesaIsAtrasado() {
        CategoriaDTO categoriaDTO = categoriaDataProvider.createCategoria("Familia");

        DespesaDTO despesaDTO = new DespesaDTO(UUID.randomUUID(), "Presente Natal", BigDecimal.valueOf(4000), LocalDate.now().minusDays(1),
                false, categoriaDTO.uuidCategoria(), categoriaDTO.usuarioFK());

        Despesa despesa = new Despesa(despesaDTO);

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(despesa.isAtrasado()).isEqualTo(true);
        });
    }

    @Test
    public void despesaIsEmDia() {
        CategoriaDTO categoriaDTO = categoriaDataProvider.createCategoria("QualCoisa");

        DespesaDTO despesaDTO = new DespesaDTO(UUID.randomUUID(), "Isso", BigDecimal.valueOf(4000), LocalDate.now(),
                true, categoriaDTO.uuidCategoria(), categoriaDTO.usuarioFK());

        Despesa despesa = new Despesa(despesaDTO);

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(despesa.isEmDia()).isEqualTo(true);
        });
    }

    @Test
    public void updateDespesa() {
        CategoriaDTO categoriaDTO = categoriaDataProvider.createCategoria("QualCoisa3");

        DespesaDTO despesaDTO = new DespesaDTO(UUID.randomUUID(), "Isso2", BigDecimal.valueOf(890), LocalDate.now().minusDays(1),
                false, categoriaDTO.uuidCategoria(), categoriaDTO.usuarioFK());

        Despesa despesa = new Despesa(despesaDTO);

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(despesa.isAtrasado()).isEqualTo(true);
            s.assertThat(despesa.getDataVencimento()).isBefore(LocalDate.now());
            s.assertThat(despesa.getPago()).isEqualTo(false);
        });

        DespesaDTO toReturnResponse = despesaService.createDespesa(despesaDTO);

        long before = despesaRepository.count();

        DespesaDTO novaDespesaDTO = new DespesaDTO(toReturnResponse.uuidDespesa(), "nova despesa", BigDecimal.valueOf(890), LocalDate.now().minusDays(1),
                true, toReturnResponse.categoriaFK(), toReturnResponse.usuarioFK());

        DespesaDTO despesaAtualizada = despesaService.updateDespesa(novaDespesaDTO, novaDespesaDTO.uuidDespesa());

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
    public void updateDespesaIsNull() {
        CategoriaDTO categoriaDTO = categoriaDataProvider.createCategoria("Outros");
        UsuarioDTO usuarioDTO = usuarioDataProvider.createUsuarioTO();
        despesaDataProvider.createDespesaCustom("Viajens", "Horlando");

        DespesaDTO despesaDTO = new DespesaDTO(UUID.randomUUID(), "surpresa niver", BigDecimal.valueOf(200), LocalDate.now().plusDays(15), false, categoriaDTO.uuidCategoria(), usuarioDTO.uuid());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> despesaService.updateDespesa(despesaDTO, despesaDTO.uuidDespesa())).isInstanceOf(IsNull.class)
                    .hasMessage("Despesa não encontrada.");
        });
    }

    @Test
    @Transactional
    public void deleteDespesa() {
        despesaRepository.deleteAll();
        DespesaDTO despesaCreated = despesaDataProvider.createDespesaCustom("Sogra", "Buque");
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
    public void deleteDespesaIsNull() {
        despesaRepository.deleteAll();

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> despesaService.deleteDespesa(null)).isInstanceOf(IsNull.class)
                    .hasMessage("Despesa não encontrada.");
        });
    }
}
