package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.DataSourceFactory;
import hexlet.code.util.DatabaseInitializer;
import io.javalin.testtools.JavalinTest;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
    private static MockWebServer mockWebServer;

    @BeforeAll
    static void beforeAll() throws Exception {
        mockWebServer = new MockWebServer();
        mockWebServer.start();
    }

    @AfterAll
    static void afterAll() throws Exception {
        mockWebServer.close();
    }

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
            Assertions.assertTrue(response.body().contains(url.getName()));
        });
    }

    @Test
    void testUrlsPageShowsLatestChecks() throws Exception {
        var app = App.getApp();

        var firstUrl = new Url("https://first-example.com");
        var secondUrl = new Url("https://second-example.com");
        UrlRepository.save(firstUrl);
        UrlRepository.save(secondUrl);

        var oldCheck = new UrlCheck(firstUrl.getId(), 201, "old-h1", "old-title", "old-description");
        UrlCheckRepository.save(oldCheck);

        var latestCheck = new UrlCheck(firstUrl.getId(), 418, "latest-h1", "latest-title", "latest-description");
        UrlCheckRepository.save(latestCheck);

        var secondUrlCheck = new UrlCheck(secondUrl.getId(), 204, "second-h1", "second-title", "second-description");
        UrlCheckRepository.save(secondUrlCheck);

        JavalinTest.test(app, (server, client) -> {
            var httpClient = HttpClient.newHttpClient();
            var requestBuilder = HttpRequest.newBuilder();
            requestBuilder.uri(URI.create(client.getOrigin() + "/urls"));
            requestBuilder.GET();
            var request = requestBuilder.build();

            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            var body = response.body();

            Assertions.assertEquals(200, response.statusCode());
            Assertions.assertTrue(body.contains("data-test=\"urls\""));
            Assertions.assertTrue(body.contains(firstUrl.getName()));
            Assertions.assertTrue(body.contains(secondUrl.getName()));
            Assertions.assertTrue(body.contains("418"));
            Assertions.assertTrue(body.contains("204"));
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

    @Test
    void testCheckUrlSuccess() throws Exception {
        var app = App.getApp();

        var longText = "a".repeat(205);
        var html = "<html><head><title>" + longText + "</title>"
                + "<meta name=\"description\" content=\"" + longText + "\"></head>"
                + "<body><h1>" + longText + "</h1></body></html>";

        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(html));

        JavalinTest.test(app, (server, client) -> {
            var httpClientBuilder = HttpClient.newBuilder();
            httpClientBuilder.followRedirects(HttpClient.Redirect.NEVER);
            var httpClient = httpClientBuilder.build();

            var normalizedBaseUrl = getMockServerBaseUrl();

            var createRequest = buildPostRequest(
                    client.getOrigin() + "/urls",
                    "url=" + normalizedBaseUrl + "/pages"
            );
            httpClient.send(createRequest, HttpResponse.BodyHandlers.ofString());

            var savedUrl = UrlRepository.findByName(normalizedBaseUrl);
            Assertions.assertTrue(savedUrl.isPresent());

            var checkRequest = buildPostRequest(
                    client.getOrigin() + "/urls/" + savedUrl.get().getId() + "/checks",
                    ""
            );
            var checkResponse = httpClient.send(checkRequest, HttpResponse.BodyHandlers.ofString());

            Assertions.assertEquals(302, checkResponse.statusCode());
            Assertions.assertEquals(
                    "/urls/" + savedUrl.get().getId(),
                    checkResponse.headers().firstValue("Location").orElse("")
            );

            var checks = UrlCheckRepository.findByUrlId(savedUrl.get().getId());
            Assertions.assertEquals(1, checks.size());

            var check = checks.get(0);
            Assertions.assertEquals(200, check.getStatusCode());
            Assertions.assertEquals(longText, check.getH1());
            Assertions.assertEquals(longText, check.getTitle());
            Assertions.assertEquals(longText, check.getDescription());
        });
    }

    @Test
    void testCheckUrlError() throws Exception {
        var app = App.getApp();

        mockWebServer.enqueue(new MockResponse().setResponseCode(500).setBody("error"));

        JavalinTest.test(app, (server, client) -> {
            var httpClientBuilder = HttpClient.newBuilder();
            httpClientBuilder.followRedirects(HttpClient.Redirect.NEVER);
            var httpClient = httpClientBuilder.build();

            var normalizedBaseUrl = getMockServerBaseUrl();

            var createRequest = buildPostRequest(
                    client.getOrigin() + "/urls",
                    "url=" + normalizedBaseUrl + "/pages"
            );
            httpClient.send(createRequest, HttpResponse.BodyHandlers.ofString());

            var savedUrl = UrlRepository.findByName(normalizedBaseUrl);
            Assertions.assertTrue(savedUrl.isPresent());

            var checkRequest = buildPostRequest(
                    client.getOrigin() + "/urls/" + savedUrl.get().getId() + "/checks",
                    ""
            );
            var checkResponse = httpClient.send(checkRequest, HttpResponse.BodyHandlers.ofString());

            Assertions.assertEquals(302, checkResponse.statusCode());
            Assertions.assertEquals(
                    "/urls/" + savedUrl.get().getId(),
                    checkResponse.headers().firstValue("Location").orElse("")
            );

            var checks = UrlCheckRepository.findByUrlId(savedUrl.get().getId());
            Assertions.assertEquals(0, checks.size());
        });
    }

    @Test
    void testFindLatestChecksReturnsLatestCheckForEachUrl() throws Exception {
        var firstUrl = new Url("https://latest-first.com");
        var secondUrl = new Url("https://latest-second.com");
        UrlRepository.save(firstUrl);
        UrlRepository.save(secondUrl);

        var firstOldCheck = new UrlCheck(firstUrl.getId(), 200, "first-old-h1", "first-old-title", "first-old-description");
        UrlCheckRepository.save(firstOldCheck);

        var firstLatestCheck = new UrlCheck(
                firstUrl.getId(),
                301,
                "first-latest-h1",
                "first-latest-title",
                "first-latest-description"
        );
        UrlCheckRepository.save(firstLatestCheck);

        var secondLatestCheck = new UrlCheck(
                secondUrl.getId(),
                404,
                "second-latest-h1",
                "second-latest-title",
                "second-latest-description"
        );
        UrlCheckRepository.save(secondLatestCheck);

        var latestChecks = UrlCheckRepository.findLatestChecks();

        Assertions.assertEquals(2, latestChecks.size());
        Assertions.assertEquals(301, latestChecks.get(firstUrl.getId()).getStatusCode());
        Assertions.assertEquals("first-latest-h1", latestChecks.get(firstUrl.getId()).getH1());
        Assertions.assertEquals(404, latestChecks.get(secondUrl.getId()).getStatusCode());
        Assertions.assertEquals("second-latest-h1", latestChecks.get(secondUrl.getId()).getH1());
    }

    @Test
    void testFindLatestChecksReturnsEmptyMapWhenNoChecks() throws Exception {
        var latestChecks = UrlCheckRepository.findLatestChecks();

        Assertions.assertTrue(latestChecks.isEmpty());
    }

    private HttpRequest buildPostRequest(String url, String formData) {
        var requestBuilder = HttpRequest.newBuilder();
        requestBuilder.uri(URI.create(url));
        requestBuilder.header("Content-Type", "application/x-www-form-urlencoded");
        requestBuilder.POST(HttpRequest.BodyPublishers.ofString(encodeForm(formData)));
        return requestBuilder.build();
    }

    private String encodeForm(String formData) {
        if (formData.isEmpty()) {
            return "";
        }

        var parts = formData.split("&");
        var result = new StringBuilder();
        var isFirst = true;

        for (var part : parts) {
            var keyValue = part.split("=", 2);

            if (!isFirst) {
                result.append("&");
            }

            result.append(URLEncoder.encode(keyValue[0], StandardCharsets.UTF_8));
            result.append("=");
            result.append(URLEncoder.encode(keyValue[1], StandardCharsets.UTF_8));
            isFirst = false;
        }

        return result.toString();
    }

    private String getMockServerBaseUrl() {
        var uri = URI.create(mockWebServer.url("/").toString());
        return String.format("%s://%s:%d", uri.getScheme(), uri.getHost(), uri.getPort());
    }
}
