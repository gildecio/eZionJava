package com.estoque.repository;

import com.estoque.model.NotaFiscalEntradaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotaFiscalEntradaItemRepository extends JpaRepository<NotaFiscalEntradaItem, Long> {
    List<NotaFiscalEntradaItem> findByNotaFiscalEntradaId(Long notaFiscalEntradaId);
}
