package com.mfinancas.api.repository.usuario;

import com.mfinancas.api.api.dto.usuario.UsuarioDTO;
import com.mfinancas.api.domain.entity.usuario.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepositoryCustomImpl implements UsuarioRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Usuario findByUser(UsuarioDTO usuarioDTO) {
        String jpql = """
                    SELECT user FROM Usuario user
                    WHERE user.email = :email
                    AND user.senha = :senha
                """;

        return em.createQuery(jpql, Usuario.class)
                .setParameter("email", usuarioDTO.email())
                .setParameter("senha", usuarioDTO.senha())
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
