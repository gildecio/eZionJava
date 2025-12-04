package com.estoque.repository;

import com.estoque.model.NotaFiscalSaidaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotaFiscalSaidaItemRepository extends JpaRepository<NotaFiscalSaidaItem, Long> {
    List<NotaFiscalSaidaItem> findByNotaFiscalSaidaId(Long notaFiscalSaidaId);
}
