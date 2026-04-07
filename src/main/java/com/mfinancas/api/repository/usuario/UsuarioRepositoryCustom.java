package com.mfinancas.api.repository.usuario;

import com.mfinancas.api.api.dto.usuario.UsuarioDTO;
import com.mfinancas.api.domain.entity.usuario.Usuario;

public interface UsuarioRepositoryCustom {
    Usuario findByUser(UsuarioDTO usuario);
}
