package com.mfinancas.api.api.controller.usuario;

import com.mfinancas.api.api.dto.usuario.UsuarioDTO;
import com.mfinancas.api.service.usuario.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("controle-financeiro")
public class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioDTO> createUsuario(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO toReturn = usuarioService.createUsuario(usuarioDTO);
        return ResponseEntity.ok(toReturn);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        return ResponseEntity.ok(usuarioService.getAll());
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> login(@Valid @RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO toReturnUsuario = usuarioService.postLogin(usuarioDTO);
        return ResponseEntity.ok().body(toReturnUsuario);
    }

    @DeleteMapping("/delete-usuario/{uuidUser}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID uuidUser){
        usuarioService.deleteUsuario(uuidUser);
        return ResponseEntity.ok().body("Usuário deletado com sucesso.");
    }
}
