package com.estoque.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;

public class EmbalagemTest {
    @Test
    void testFatorSetterGetter() {
        Embalagem embalagem = new Embalagem();
        embalagem.setFator(new BigDecimal("2.5"));
        assertEquals(new BigDecimal("2.5"), embalagem.getFator());
    }
}
