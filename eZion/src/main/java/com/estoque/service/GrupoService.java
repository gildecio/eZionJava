package com.estoque.service;

import com.estoque.model.Grupo;
import com.estoque.repository.GrupoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class GrupoService {

    @Autowired
    private GrupoRepository grupoRepository;

    public Grupo createGrupo(Grupo grupo) {
        return grupoRepository.save(grupo);
    }

    public List<Grupo> getAllGrupos() {
        return grupoRepository.findAll();
    }

    public Optional<Grupo> getGrupoById(Long id) {
        return grupoRepository.findById(id);
    }

    public Grupo updateGrupo(Long id, Grupo grupoDetails) {
        return grupoRepository.findById(id).map(grupo -> {
            grupo.setNome(grupoDetails.getNome());
            grupo.setDescricao(grupoDetails.getDescricao());
            grupo.setGrupoPai(grupoDetails.getGrupoPai());
            grupo.setNcm(grupoDetails.getNcm());
            return grupoRepository.save(grupo);
        }).orElseThrow(() -> new RuntimeException("Grupo not found with id " + id));
    }

    public void deleteGrupo(Long id) {
        grupoRepository.deleteById(id);
    }

    public List<Grupo> getGruposByGrupoPai(Long grupoId) {
        return grupoRepository.findByGrupoPaiId(grupoId);
    }

    public List<Grupo> getRootGrupos() {
        return grupoRepository.findByGrupoPaiIsNull();
    }
}
