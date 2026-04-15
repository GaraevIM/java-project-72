package hexlet.code.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class UrlCheckTest {

    @Test
    public void testConstructorWithAllFields() {
        var createdAt = LocalDateTime.now();

        var urlCheck = new UrlCheck(
                1L,
                2L,
                200,
                "h1",
                "title",
                "description",
                createdAt
        );

        Assertions.assertEquals(1L, urlCheck.getId());
        Assertions.assertEquals(2L, urlCheck.getUrlId());
        Assertions.assertEquals(200, urlCheck.getStatusCode());
        Assertions.assertEquals("h1", urlCheck.getH1());
        Assertions.assertEquals("title", urlCheck.getTitle());
        Assertions.assertEquals("description", urlCheck.getDescription());
        Assertions.assertEquals(createdAt, urlCheck.getCreatedAt());
    }

    @Test
    public void testConstructorWithoutIdAndSetters() {
        var createdAt = LocalDateTime.now();

        var urlCheck = new UrlCheck(
                3L,
                404,
                "new h1",
                "new title",
                "new description"
        );

        urlCheck.setId(10L);
        urlCheck.setUrlId(20L);
        urlCheck.setStatusCode(201);
        urlCheck.setH1("updated h1");
        urlCheck.setTitle("updated title");
        urlCheck.setDescription("updated description");
        urlCheck.setCreatedAt(createdAt);

        Assertions.assertEquals(10L, urlCheck.getId());
        Assertions.assertEquals(20L, urlCheck.getUrlId());
        Assertions.assertEquals(201, urlCheck.getStatusCode());
        Assertions.assertEquals("updated h1", urlCheck.getH1());
        Assertions.assertEquals("updated title", urlCheck.getTitle());
        Assertions.assertEquals("updated description", urlCheck.getDescription());
        Assertions.assertEquals(createdAt, urlCheck.getCreatedAt());
    }
}
