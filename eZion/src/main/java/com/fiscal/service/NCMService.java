package com.fiscal.service;

import com.fiscal.model.NCM;
import com.fiscal.repository.NCMRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NCMService {

    @Autowired
    private NCMRepository ncmRepository;

    public List<NCM> findAll() {
        return ncmRepository.findAll();
    }

    public Optional<NCM> findById(Long id) {
        return ncmRepository.findById(id);
    }

    public NCM save(NCM ncm) {
        return ncmRepository.save(ncm);
    }

    public void deleteById(Long id) {
        ncmRepository.deleteById(id);
    }
}
