package hexlet.code.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class StringUtilsTest {

    @Test
    public void testTruncateWithNull() {
        assertEquals("", StringUtils.truncate(null));
    }

    @Test
    public void testTruncateWithShortString() {
        String value = "hello";
        assertEquals("hello", StringUtils.truncate(value));
    }

    @Test
    public void testTruncateWithExactMaxLength() {
        String value = "a".repeat(200);
        assertEquals(value, StringUtils.truncate(value));
    }

    @Test
    public void testTruncateWithLongString() {
        String value = "a".repeat(201);
        String expected = "a".repeat(200) + "...";
        assertEquals(expected, StringUtils.truncate(value));
    }
}
