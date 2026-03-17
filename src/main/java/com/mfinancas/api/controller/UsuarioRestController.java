package com.mfinancas.api.controller;

import com.mfinancas.api.dto.UsuarioDTO;
import com.mfinancas.api.service.UsuarioService;
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
    public ResponseEntity<UsuarioDTO> createUsuario(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO toReturn = usuarioService.createUsuario(usuarioDTO);
        return ResponseEntity.ok(toReturn);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDTO>> findAll() {
        return ResponseEntity.ok(usuarioService.getAll());
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioDTO> login(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO toReturnUsuario = usuarioService.postLogin(usuarioDTO);
        return ResponseEntity.ok().body(toReturnUsuario);
    }

    @DeleteMapping("/delete-usuario/{uuidUser}")
    public ResponseEntity<String> deleteUser(@PathVariable UUID uuidUser){
        usuarioService.deleteUsuario(uuidUser);
        return ResponseEntity.ok().body("Usuário deletado com sucesso.");
    }
}
