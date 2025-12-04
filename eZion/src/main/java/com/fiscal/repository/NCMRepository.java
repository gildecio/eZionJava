package com.fiscal.repository;

import com.fiscal.model.NCM;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NCMRepository extends JpaRepository<NCM, Long> {
}
