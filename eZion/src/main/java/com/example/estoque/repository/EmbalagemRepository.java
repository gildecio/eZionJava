package com.example.estoque.repository;

import com.example.estoque.model.Embalagem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmbalagemRepository extends JpaRepository<Embalagem, Long> {
}