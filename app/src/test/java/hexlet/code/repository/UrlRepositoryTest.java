package hexlet.code.repository;

import hexlet.code.model.Url;
import hexlet.code.util.DataSourceFactory;
import hexlet.code.util.DatabaseInitializer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UrlRepositoryTest {
    @BeforeEach
    void setUp() throws Exception {
        var dataSource = DataSourceFactory.getDataSource();
        DatabaseInitializer.init(dataSource);
        BaseRepository.setDataSource(dataSource);
    }

    @Test
    void testSaveAndFind() throws Exception {
        var url = new Url("https://example.com");
        UrlRepository.save(url);

        var savedUrl = UrlRepository.find(url.getId());

        Assertions.assertTrue(savedUrl.isPresent());
        Assertions.assertEquals("https://example.com", savedUrl.get().getName());
        Assertions.assertNotNull(savedUrl.get().getCreatedAt());
    }

    @Test
    void testFindByName() throws Exception {
        var url = new Url("https://hexlet.io");
        UrlRepository.save(url);

        var savedUrl = UrlRepository.findByName("https://hexlet.io");

        Assertions.assertTrue(savedUrl.isPresent());
        Assertions.assertEquals("https://hexlet.io", savedUrl.get().getName());
    }
}
