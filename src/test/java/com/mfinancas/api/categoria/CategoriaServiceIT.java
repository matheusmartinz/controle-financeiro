package com.mfinancas.api.categoria;

import com.mfinancas.api.TipoCategoria;
import com.mfinancas.api.dataprovider.CategoriaDataProvider;
import com.mfinancas.api.dataprovider.UsuarioDataProvider;
import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.exceptions.FailedConditional;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.model.Categoria;
import com.mfinancas.api.service.CategoriaRepository;
import com.mfinancas.api.service.CategoriaService;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@SpringBootTest
@ActiveProfiles("test")
public class CategoriaServiceIT {
    @Autowired
    CategoriaRepository categoriaRepository;

    @Autowired
    CategoriaService categoriaService;

    @Autowired
    UsuarioDataProvider usuarioDataProvider;

    @Autowired
    CategoriaDataProvider categoriaDataProvider;

    @Test
    public void createCategoria() {
        UsuarioTO usuarioResponse = usuarioDataProvider.createUsuarioTO();

        CategoriaTO categoriaTO = new CategoriaTO(
                UUID.randomUUID(),
                "Moto",
                TipoCategoria.DESPESA,
                usuarioResponse.uuid()
        );
        long before = categoriaRepository.count();

        CategoriaTO toResponseCategoria = categoriaService.createCategoria(categoriaTO, categoriaTO.usuarioFK());

        long after = categoriaRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(toResponseCategoria.nome()).isNotNull();
            s.assertThat(toResponseCategoria.tipo()).isEqualTo(TipoCategoria.DESPESA);
            s.assertThat(after).isEqualTo(before + 1);
        });
    }

    @Test
    @SneakyThrows
    public void categoriaNotFound() {
        CategoriaTO categoriaTO = new CategoriaTO(UUID.randomUUID(), "Caminhão", TipoCategoria.DESPESA, null);


        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> categoriaService.createCategoria(categoriaTO, categoriaTO.usuarioFK()))
                    .isInstanceOf(IsNull.class)
                    .hasMessage("Usuário não encontrado.");
        });
    }

    @Test
    @SneakyThrows
    public void categoriaSameName() {
        UsuarioTO usuarioTO = usuarioDataProvider.createUsuarioTO();

        CategoriaTO categoriaTO = new CategoriaTO(UUID.randomUUID(), "Caminhão", TipoCategoria.DESPESA, usuarioTO.uuid());

        categoriaService.createCategoria(categoriaTO, usuarioTO.uuid());

        CategoriaTO categoriaTOSameName = new CategoriaTO(UUID.randomUUID(), "Caminhão", TipoCategoria.DESPESA, usuarioTO.uuid());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> categoriaService.createCategoria(categoriaTOSameName, usuarioTO.uuid()))
                    .isInstanceOf(FailedConditional.class)
                    .hasMessage("Já existe um nome cadastrado com essa categoria.");
        });
    }

    @Test
    @SneakyThrows
    public void categoriaNomeVazio() {
        UsuarioTO usuarioTO = usuarioDataProvider.createUsuarioTO();

        CategoriaTO categoriaTO = new CategoriaTO(UUID.randomUUID(), "", TipoCategoria.DESPESA, usuarioTO.uuid());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> categoriaService.createCategoria(categoriaTO, usuarioTO.uuid()))
                    .isInstanceOf(FailedConditional.class)
                    .hasMessage("Obrigatório informar o descricao da categoria.");
        });
    }

    @Test
    @SneakyThrows
    public void categoriaTipoNull() {
        UsuarioTO usuarioTO = usuarioDataProvider.createUsuarioTO();

        CategoriaTO categoriaTO = new CategoriaTO(UUID.randomUUID(), "Grama", null, usuarioTO.uuid());

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> categoriaService.createCategoria(categoriaTO, usuarioTO.uuid()))
                    .isInstanceOf(FailedConditional.class)
                    .hasMessage("Obrigatório informar o  tipo da categoria.");
        });
    }

    @Test
    public void updateCategoria() {
        categoriaRepository.deleteAll();
        UsuarioTO usuarioTO = usuarioDataProvider.createUsuarioTO();

        long before = categoriaRepository.count();
        CategoriaTO categoriaResponse = categoriaDataProvider.createCategoria("Fazenda");
        Categoria categoriaSaved = categoriaRepository.findByUuid(categoriaResponse.uuidCategoria());
        long after = categoriaRepository.count();

        CategoriaTO categoriaUpdate = new CategoriaTO(categoriaSaved.getUuid(), "Fazendinha", TipoCategoria.DESPESA, usuarioTO.uuid());
        long afterUpdate = categoriaRepository.count();
        CategoriaTO categoriaUpdated = categoriaService.updateCategoria(categoriaUpdate, categoriaResponse.uuidCategoria());

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(after).isEqualTo(before + 1);
            s.assertThat(afterUpdate).isEqualTo(after);

            s.assertThat(categoriaUpdated.uuidCategoria()).isEqualTo(categoriaSaved.getUuid());

        });
    }

    @Test
    @SneakyThrows
    public void updateCategoriaIsNull() {
        usuarioDataProvider.createUsuarioTO();

        categoriaDataProvider.createCategoria("Fazenda2");

        CategoriaTO categoriaUpdate = categoriaDataProvider.createCategoria("Fazendinha3");

        SoftAssertions.assertSoftly(s -> {
            s.assertThatThrownBy(() -> categoriaService.updateCategoria(categoriaUpdate, UUID.randomUUID())).isInstanceOf(IsNull.class)
                    .hasMessage("Categoria não encontrada.");
        });
    }

    @Test
    @Transactional
    public void deleteCategoria() {
        categoriaRepository.deleteAll();
        CategoriaTO categoriaSaved = categoriaDataProvider.createCategoria("TestedeDeletar");
        long afterCreated = categoriaRepository.count();

        categoriaService.deleteCategoria(categoriaSaved.uuidCategoria());
        long afterDeleted = categoriaRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(afterCreated).isEqualTo(1);
            s.assertThat(afterDeleted).isEqualTo(0);
        });
    }

    @Test
    public void deleteCategoriaIsNull() {
        categoriaDataProvider.createCategoria("TesteDelete2");

        SoftAssertions.assertSoftly(s -> {

            s.assertThatThrownBy(() -> categoriaService.deleteCategoria(UUID.randomUUID())).isInstanceOf(IsNull.class)
                    .hasMessage("Categoria não encontrada.");
        });
    }
}
