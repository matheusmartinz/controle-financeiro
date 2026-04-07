package com.mfinancas.api.service.usuario;

import com.mfinancas.api.api.dto.usuario.UsuarioDTO;
import com.mfinancas.api.exceptions.IsNull;
import com.mfinancas.api.domain.entity.usuario.Usuario;
import com.mfinancas.api.repository.usuario.UsuarioRepository;
import com.mfinancas.api.repository.usuario.UsuarioRepositoryCustomImpl;
import com.mfinancas.api.service.superservice.SuperServiceSave;
import com.mfinancas.api.utils.ValidateEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UsuarioService extends SuperServiceSave<Usuario, UsuarioRepository> {
    @Autowired
    private UsuarioRepositoryCustomImpl usuarioRepositoryCustomImpl;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository repository) {
        super(repository);
    }

    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        ValidateEmail.validaEmail(usuarioDTO.email());
        Usuario usuario = new Usuario(usuarioDTO);
        return new UsuarioDTO(save(usuario));
    }

    public UsuarioDTO postLogin(UsuarioDTO usuarioDTO) {
        ValidateEmail.validaEmail(usuarioDTO.email());
        Usuario userEntity = usuarioRepositoryCustomImpl.findByUser(usuarioDTO);
        if (userEntity == null) {
            throw new IsNull("Credenciais incorretos.");
        }
        return new UsuarioDTO(userEntity);
    }

    public List<UsuarioDTO> getAll() {
        List<Usuario> listUsers = usuarioRepository.findAll();
        return listUsers.stream().map(UsuarioDTO::new).toList();
    }

    public void deleteUsuario(UUID uuidUser) {
        if (usuarioRepository.findByUuid(uuidUser) == null) {
            throw new IsNull("Usuário não encontrado.");
        }
        usuarioRepository.deleteByUuid(uuidUser);
    }
}
