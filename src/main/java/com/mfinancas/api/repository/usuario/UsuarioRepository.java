package com.mfinancas.api.repository.usuario;

import com.mfinancas.api.domain.entity.usuario.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByUuid(UUID uuidUsuario);

    UUID uuid(UUID uuid);

    void deleteByUuid(UUID uuidUser);
}
