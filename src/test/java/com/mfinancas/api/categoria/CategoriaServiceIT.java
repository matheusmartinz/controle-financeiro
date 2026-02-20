package com.mfinancas.api.categoria;

import com.mfinancas.api.TipoCategoria;
import com.mfinancas.api.dataprovider.UsuarioCreateDataProvider;
import com.mfinancas.api.dto.CategoriaTO;
import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.service.CategoriaRepository;
import com.mfinancas.api.service.CategoriaService;
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
    UsuarioCreateDataProvider  usuarioCreateDataProvider;

    @Test
    public void createCategoria() {
        UsuarioTO usuarioResponse = usuarioCreateDataProvider.createUsuarioTO();

        CategoriaTO categoriaTO = new CategoriaTO(
                UUID.randomUUID(),
                "Lazer",
                TipoCategoria.DESPESA,
                usuarioResponse.uuidUsuario()
        );
        long before = categoriaRepository.count();

        CategoriaTO toResponseCategoria = categoriaService.createCategoria(categoriaTO, categoriaTO.usuarioFK());

        long after = categoriaRepository.count();

        SoftAssertions.assertSoftly(s -> {
            s.assertThat(toResponseCategoria.descricao()).isNotNull();
            s.assertThat(toResponseCategoria.tipo()).isEqualTo(TipoCategoria.DESPESA);
            s.assertThat(after).isEqualTo(before + 1);
        });
    }
}
