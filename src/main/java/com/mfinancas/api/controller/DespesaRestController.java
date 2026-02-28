package com.mfinancas.api.controller;

import com.mfinancas.api.dto.DespesaTO;
import com.mfinancas.api.service.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("despesa")
public class DespesaRestController {

    @Autowired
    private DespesaService despesaService;

    @GetMapping
    public ResponseEntity<List<DespesaTO>> getAllDespesas(){
        return ResponseEntity.ok(despesaService.findAllDespesas());
    }

    @PostMapping("/cadastro")
    public ResponseEntity<DespesaTO> createDespesa(@RequestBody DespesaTO despesaTO){
        return ResponseEntity.ok(despesaService.createDespesa(despesaTO));
    }

    @PutMapping("/editar/{uuidDespesa}")
    public ResponseEntity<DespesaTO> updateDespesa(@RequestBody DespesaTO despesaTO, @PathVariable UUID uuidDespesa){
        return ResponseEntity.ok(despesaService.updateDespesa(despesaTO, uuidDespesa));
    }
}
