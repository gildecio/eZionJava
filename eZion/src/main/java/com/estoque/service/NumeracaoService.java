package com.estoque.service;

import com.estoque.model.NumeracaoDocumento;
import com.estoque.repository.NumeracaoDocumentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.Date;

@Service
public class NumeracaoService {

    @Autowired
    private NumeracaoDocumentoRepository numeracaoDocumentoRepository;

    @Transactional
    public String gerarNumero(NumeracaoDocumento.TipoDocumento tipoDocumento) {
        NumeracaoDocumento numeracao = numeracaoDocumentoRepository
                .findByTipoDocumento(tipoDocumento)
                .orElseGet(() -> {
                    NumeracaoDocumento nova = new NumeracaoDocumento(tipoDocumento);
                    return numeracaoDocumentoRepository.save(nova);
                });

        int anoAtual = LocalDate.now().getYear();

        // Reset anual
        if (!numeracao.getAnoVigente().equals(anoAtual)) {
            numeracao.setProximoNumero(1L);
            numeracao.setAnoVigente(anoAtual);
        }

        String numero = String.format("%s-%d-%06d",
                tipoDocumento.getPrefixo(),
                anoAtual,
                numeracao.getProximoNumero()
        );

        numeracao.setProximoNumero(numeracao.getProximoNumero() + 1);
        numeracao.setUltimaAtualizacao(new Date());
        numeracaoDocumentoRepository.save(numeracao);

        return numero;
    }

    public NumeracaoDocumento getNumeracao(NumeracaoDocumento.TipoDocumento tipoDocumento) {
        return numeracaoDocumentoRepository.findByTipoDocumento(tipoDocumento)
                .orElse(null);
    }

    @Transactional
    public void resetarNumeracao(NumeracaoDocumento.TipoDocumento tipoDocumento) {
        NumeracaoDocumento numeracao = numeracaoDocumentoRepository
                .findByTipoDocumento(tipoDocumento)
                .orElse(null);

        if (numeracao != null) {
            numeracao.setProximoNumero(1L);
            numeracao.setAnoVigente(LocalDate.now().getYear());
            numeracao.setUltimaAtualizacao(new Date());
            numeracaoDocumentoRepository.save(numeracao);
        }
    }

    public void inicializarNumeracoes() {
        for (NumeracaoDocumento.TipoDocumento tipo : NumeracaoDocumento.TipoDocumento.values()) {
            if (!numeracaoDocumentoRepository.findByTipoDocumento(tipo).isPresent()) {
                NumeracaoDocumento nova = new NumeracaoDocumento(tipo);
                numeracaoDocumentoRepository.save(nova);
            }
        }
    }
}
