package com.mfinancas.api.repository;

import com.mfinancas.api.dto.UsuarioDTO;
import com.mfinancas.api.model.Usuario;

public interface UsuarioRepositoryCustom {
    Usuario findByUser(UsuarioDTO usuario);
}
