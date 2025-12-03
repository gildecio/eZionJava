package com.estoque.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LocalTest {
    @Test
    void testNomeSetterGetter() {
        Local local = new Local();
        local.setNome("Almoxarifado");
        assertEquals("Almoxarifado", local.getNome());
    }
}
