package com.mfinancas.api.repository;

import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.model.Usuario;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepositoryCustomImpl implements UsuarioRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Usuario findByUser(UsuarioTO usuarioTO) {
        String jpql = """
                    SELECT user FROM Usuario user
                    WHERE user.email = :email
                    AND user.senha = :senha
                """;

        return em.createQuery(jpql, Usuario.class)
                .setParameter("email", usuarioTO.email())
                .setParameter("senha", usuarioTO.senha())
                .getResultStream()
                .findFirst()
                .orElse(null);
    }
}
