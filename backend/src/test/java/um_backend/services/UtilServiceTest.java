package um_backend.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilServiceTest {

    private final UtilService utilService = new UtilService();

    @Test
    void idGenerator_shouldReturnUniqueString_whenCalled() {
        String id = utilService.generateId();
        assertFalse(id.isEmpty(), "Die generierte ID sollte nicht leer sein");
        assertTrue(id.matches("^[0-9a-fA-F-]{36}$"), "Die generierte ID sollte das UUID-Format haben");
    }
}