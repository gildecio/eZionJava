package com.example.estoque.service;

import com.example.estoque.model.Embalagem;
import com.example.estoque.repository.EmbalagemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmbalagemService {

    @Autowired
    private EmbalagemRepository embalagemRepository;

    public List<Embalagem> findAll() {
        return embalagemRepository.findAll();
    }

    public Optional<Embalagem> findById(Long id) {
        return embalagemRepository.findById(id);
    }

    public Embalagem save(Embalagem embalagem) {
        return embalagemRepository.save(embalagem);
    }

    public void deleteById(Long id) {
        embalagemRepository.deleteById(id);
    }
}