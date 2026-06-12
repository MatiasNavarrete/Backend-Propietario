package com.example.propietario.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BaseEntityTest {

    @Test
    @DisplayName("Debería instanciar BaseEntity y probar sus getters y setters generados por Lombok")
    void testBaseEntityGettersAndSetters() {
        BaseEntity baseEntity = new BaseEntity();
        UUID fakeId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        baseEntity.setId(fakeId);
        baseEntity.setCreatedAt(now);
        baseEntity.setUpdateAt(now);
        baseEntity.set_active(false);

        assertNotNull(baseEntity);
        assertEquals(fakeId, baseEntity.getId());
        assertEquals(now, baseEntity.getCreatedAt());
        assertEquals(now, baseEntity.getUpdateAt());
        assertFalse(baseEntity.is_active());
    }
}