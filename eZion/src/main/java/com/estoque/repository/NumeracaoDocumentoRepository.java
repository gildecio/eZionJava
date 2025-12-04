package com.estoque.repository;

import com.estoque.model.NumeracaoDocumento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface NumeracaoDocumentoRepository extends JpaRepository<NumeracaoDocumento, Long> {
    Optional<NumeracaoDocumento> findByTipoDocumento(NumeracaoDocumento.TipoDocumento tipoDocumento);
}
