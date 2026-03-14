package hexlet.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class AppTest {
    @Test
    void testGetGreeting() {
        assertEquals("Hello, World!", App.getGreeting());
    }
}
