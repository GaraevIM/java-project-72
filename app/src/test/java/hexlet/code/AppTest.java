package hexlet.code;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AppTest {
    @Test
    void testGetApp() throws Exception {
        var app = App.getApp();
        Assertions.assertNotNull(app);
    }
}
