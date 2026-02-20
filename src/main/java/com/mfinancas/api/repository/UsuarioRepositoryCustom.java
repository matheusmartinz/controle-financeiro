package com.mfinancas.api.repository;

import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.model.Usuario;

public interface UsuarioRepositoryCustom {
    Usuario findByUser(UsuarioTO usuario);
}
