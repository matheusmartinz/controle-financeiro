package com.mfinancas.api.controller;

import com.mfinancas.api.dto.DespesaDTO;
import com.mfinancas.api.service.DespesaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("despesa")
public class DespesaRestController {

    @Autowired
    private DespesaService despesaService;

    @GetMapping
    public ResponseEntity<List<DespesaDTO>> getAllDespesas(){
        return ResponseEntity.ok(despesaService.findAllDespesas());
    }

    @PostMapping("/cadastro")
    public ResponseEntity<DespesaDTO> createDespesa(@RequestBody DespesaDTO despesaDTO){
        return ResponseEntity.ok(despesaService.createDespesa(despesaDTO));
    }

    @PutMapping("/editar/{uuidDespesa}")
    public ResponseEntity<DespesaDTO> updateDespesa(@RequestBody DespesaDTO despesaDTO, @PathVariable UUID uuidDespesa){
        return ResponseEntity.ok(despesaService.updateDespesa(despesaDTO, uuidDespesa));
    }

    @DeleteMapping("/delete/{uuidDespesa}")
    public ResponseEntity<String> deleteDespesa(@PathVariable UUID uuidDespesa){
        despesaService.deleteDespesa(uuidDespesa);
        return ResponseEntity.ok().body("Deletado com sucesso.");
    }
}
