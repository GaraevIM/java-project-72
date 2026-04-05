package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.DataSourceFactory;
import hexlet.code.util.DatabaseInitializer;
import io.javalin.testtools.JavalinTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

class AppTest {
    @BeforeEach
    void setUp() throws Exception {
        var testDatabaseUrl = "jdbc:h2:mem:" + UUID.randomUUID() + ";DB_CLOSE_DELAY=-1;";
        System.setProperty("JDBC_DATABASE_URL", testDatabaseUrl);

        var dataSource = DataSourceFactory.getDataSource();
        DatabaseInitializer.init(dataSource);
        BaseRepository.setDataSource(dataSource);
    }

    @Test
    void testRootPage() throws Exception {
        var app = App.getApp();

        JavalinTest.test(app, (server, client) -> {
            var httpClient = HttpClient.newHttpClient();
            var requestBuilder = HttpRequest.newBuilder();
            requestBuilder.uri(URI.create(client.getOrigin() + "/"));
            requestBuilder.GET();
            var request = requestBuilder.build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            Assertions.assertEquals(200, response.statusCode());
            Assertions.assertTrue(response.body().contains("Анализатор страниц"));
            Assertions.assertTrue(response.body().contains("name=\"url\""));
        });
    }

    @Test
    void testCreateUrl() throws Exception {
        var app = App.getApp();

        JavalinTest.test(app, (server, client) -> {
            var httpClientBuilder = HttpClient.newBuilder();
            httpClientBuilder.followRedirects(HttpClient.Redirect.NEVER);
            var httpClient = httpClientBuilder.build();

            var request = buildPostRequest(
                    client.getOrigin() + "/urls",
                    "url=https://ru.hexlet.io/courses"
            );

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            Assertions.assertEquals(302, response.statusCode());

            var savedUrl = UrlRepository.findByName("https://ru.hexlet.io");
            Assertions.assertTrue(savedUrl.isPresent());

            var location = response.headers().firstValue("Location").orElse("");
            Assertions.assertEquals("/urls/" + savedUrl.get().getId(), location);

            var showRequestBuilder = HttpRequest.newBuilder();
            showRequestBuilder.uri(URI.create(client.getOrigin() + location));
            showRequestBuilder.GET();
            var showRequest = showRequestBuilder.build();

            var showResponse = httpClient.send(showRequest, HttpResponse.BodyHandlers.ofString());

            Assertions.assertEquals(200, showResponse.statusCode());
            Assertions.assertTrue(showResponse.body().contains("data-test=\"url\""));
        });
    }

    @Test
    void testCreateExistingUrl() throws Exception {
        var app = App.getApp();
        var existingUrl = new Url("https://example.com");
        UrlRepository.save(existingUrl);

        JavalinTest.test(app, (server, client) -> {
            var httpClientBuilder = HttpClient.newBuilder();
            httpClientBuilder.followRedirects(HttpClient.Redirect.NEVER);
            var httpClient = httpClientBuilder.build();

            var request = buildPostRequest(
                    client.getOrigin() + "/urls",
                    "url=https://example.com/another-path"
            );

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            Assertions.assertEquals(302, response.statusCode());

            var location = response.headers().firstValue("Location").orElse("");
            Assertions.assertEquals("/urls/" + existingUrl.getId(), location);

            var urls = UrlRepository.getEntities();
            Assertions.assertEquals(1, urls.size());

            var showRequestBuilder = HttpRequest.newBuilder();
            showRequestBuilder.uri(URI.create(client.getOrigin() + location));
            showRequestBuilder.GET();
            var showRequest = showRequestBuilder.build();

            var showResponse = httpClient.send(showRequest, HttpResponse.BodyHandlers.ofString());

            Assertions.assertEquals(200, showResponse.statusCode());
            Assertions.assertTrue(showResponse.body().contains("data-test=\"url\""));
        });
    }

    @Test
    void testCreateInvalidUrl() throws Exception {
        var app = App.getApp();

        JavalinTest.test(app, (server, client) -> {
            var httpClient = HttpClient.newHttpClient();

            var request = buildPostRequest(
                    client.getOrigin() + "/urls",
                    "url=ht!tp://bad-url"
            );

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            Assertions.assertEquals(422, response.statusCode());
            Assertions.assertTrue(response.body().contains("Некорректный URL"));
        });
    }

    @Test
    void testUrlsPage() throws Exception {
        var app = App.getApp();
        var url = new Url("https://hexlet.io");
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var httpClient = HttpClient.newHttpClient();
            var requestBuilder = HttpRequest.newBuilder();
            requestBuilder.uri(URI.create(client.getOrigin() + "/urls"));
            requestBuilder.GET();
            var request = requestBuilder.build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            Assertions.assertEquals(200, response.statusCode());
            Assertions.assertTrue(response.body().contains("data-test=\"urls\""));
        });
    }

    @Test
    void testShowUrlPageContainsChecksFormAndTable() throws Exception {
        var app = App.getApp();
        var url = new Url("https://google.com");
        UrlRepository.save(url);

        JavalinTest.test(app, (server, client) -> {
            var httpClient = HttpClient.newHttpClient();
            var requestBuilder = HttpRequest.newBuilder();
            requestBuilder.uri(URI.create(client.getOrigin() + "/urls/" + url.getId()));
            requestBuilder.GET();
            var request = requestBuilder.build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            var body = response.body();

            Assertions.assertEquals(200, response.statusCode());
            Assertions.assertTrue(body.contains("data-test=\"url\""));
            Assertions.assertTrue(body.contains("data-test=\"checks\""));
            Assertions.assertTrue(body.contains("Запустить проверку"));
        });
    }

    @Test
    void testShowUrlPageNotFound() throws Exception {
        var app = App.getApp();

        JavalinTest.test(app, (server, client) -> {
            var httpClient = HttpClient.newHttpClient();
            var requestBuilder = HttpRequest.newBuilder();
            requestBuilder.uri(URI.create(client.getOrigin() + "/urls/999999"));
            requestBuilder.GET();
            var request = requestBuilder.build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            Assertions.assertEquals(404, response.statusCode());
        });
    }

    private HttpRequest buildPostRequest(String url, String formData) {
        var requestBuilder = HttpRequest.newBuilder();
        requestBuilder.uri(URI.create(url));
        requestBuilder.header("Content-Type", "application/x-www-form-urlencoded");
        requestBuilder.POST(HttpRequest.BodyPublishers.ofString(encodeForm(formData)));
        return requestBuilder.build();
    }

    private String encodeForm(String formData) {
        var parts = formData.split("&");
        var result = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            var keyValue = parts[i].split("=", 2);

            if (i > 0) {
                result.append("&");
            }

            result.append(URLEncoder.encode(keyValue[0], StandardCharsets.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(keyValue[1], StandardCharsets.UTF_8));
        }

        return result.toString();
    }
}
