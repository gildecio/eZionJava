package com.fiscal.service;

import com.fiscal.model.NaturezaOperacao;
import com.fiscal.repository.NaturezaOperacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NaturezaOperacaoService {

    @Autowired
    private NaturezaOperacaoRepository naturezaOperacaoRepository;

    public List<NaturezaOperacao> findAll() {
        return naturezaOperacaoRepository.findAll();
    }

    public Optional<NaturezaOperacao> findById(Long id) {
        return naturezaOperacaoRepository.findById(id);
    }

    public NaturezaOperacao save(NaturezaOperacao naturezaOperacao) {
        return naturezaOperacaoRepository.save(naturezaOperacao);
    }

    public void deleteById(Long id) {
        naturezaOperacaoRepository.deleteById(id);
    }
}
