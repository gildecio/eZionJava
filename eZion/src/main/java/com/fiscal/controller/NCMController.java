package com.fiscal.controller;

import com.fiscal.model.NCM;
import com.fiscal.service.NCMService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ncms")
public class NCMController {

    @Autowired
    private NCMService ncmService;

    @GetMapping
    public List<NCM> getAllNCMs() {
        return ncmService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<NCM> getNCMById(@PathVariable Long id) {
        return ncmService.findById(id)
                .map(ncm -> ResponseEntity.ok(ncm))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public NCM createNCM(@RequestBody NCM ncm) {
        return ncmService.save(ncm);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NCM> updateNCM(@PathVariable Long id, @RequestBody NCM ncmDetails) {
        return ncmService.findById(id)
                .map(ncm -> {
                    ncm.setCodigo(ncmDetails.getCodigo());
                    ncm.setDescricao(ncmDetails.getDescricao());
                    ncm.setAtivo(ncmDetails.isAtivo());
                    NCM updatedNCM = ncmService.save(ncm);
                    return ResponseEntity.ok(updatedNCM);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNCM(@PathVariable Long id) {
        if (ncmService.findById(id).isPresent()) {
            ncmService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
