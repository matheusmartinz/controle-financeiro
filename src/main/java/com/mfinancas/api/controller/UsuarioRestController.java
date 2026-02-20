package com.mfinancas.api.controller;

import com.mfinancas.api.dto.UsuarioTO;
import com.mfinancas.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("controle-financeiro")
public class UsuarioRestController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/cadastro")
    public ResponseEntity<UsuarioTO> createUsuario(@RequestBody UsuarioTO usuarioTO) {
        UsuarioTO toReturn = usuarioService.createUsuario(usuarioTO);
        return ResponseEntity.ok(toReturn);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioTO>> findAll() {
        return ResponseEntity.ok(usuarioService.getAll());
    }

    @PostMapping("/login/{uuidUsuario}")
    public ResponseEntity<String> login (@RequestBody UsuarioTO usuarioTO, @PathVariable String uuidUsuario) {
        return ResponseEntity.ok().body("Login realizado com sucesso");
    }
}
