package com.estoque.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemTest {
    @Test
    void testTipoItemSetterGetter() {
        Item item = new Item();
        item.setTipoItem(Item.TipoItem.PRODUTO);
        assertEquals(Item.TipoItem.PRODUTO, item.getTipoItem());
    }
}
