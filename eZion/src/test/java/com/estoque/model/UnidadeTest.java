package com.estoque.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UnidadeTest {
    @Test
    void testDescricaoSetterGetter() {
        Unidade unidade = new Unidade();
        unidade.setDescricao("Litros");
        assertEquals("Litros", unidade.getDescricao());
    }
}
