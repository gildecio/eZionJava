package com.estoque.repository;

import com.estoque.model.Grupo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface GrupoRepository extends JpaRepository<Grupo, Long> {
    List<Grupo> findByGrupoPaiId(Long grupoPaiId);
    List<Grupo> findByGrupoPaiIsNull();
}
