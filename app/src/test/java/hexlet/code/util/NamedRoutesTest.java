package hexlet.code.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class NamedRoutesTest {

    @Test
    public void testRootPath() {
        Assertions.assertEquals("/", NamedRoutes.rootPath());
    }

    @Test
    public void testUrlsPath() {
        Assertions.assertEquals("/urls", NamedRoutes.urlsPath());
    }

    @Test
    public void testUrlPathWithLongId() {
        Assertions.assertEquals("/urls/1", NamedRoutes.urlPath(1L));
    }

    @Test
    public void testUrlPathWithStringId() {
        Assertions.assertEquals("/urls/test", NamedRoutes.urlPath("test"));
    }

    @Test
    public void testUrlChecksPathWithLongId() {
        Assertions.assertEquals("/urls/2/checks", NamedRoutes.urlChecksPath(2L));
    }

    @Test
    public void testUrlChecksPathWithStringId() {
        Assertions.assertEquals("/urls/test/checks", NamedRoutes.urlChecksPath("test"));
    }
}
