package hexlet.code;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.resolve.ResourceCodeResolver;
import hexlet.code.model.Url;
import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.DataSourceFactory;
import hexlet.code.util.DatabaseInitializer;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;
import io.javalin.http.NotFoundResponse;
import io.javalin.rendering.template.JavalinJte;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class App {
    private static final int DEFAULT_PORT = 7070;

    private static final String DEFAULT_HOST = "0.0.0.0";

    private static final String FLASH_KEY = "flash";

    private static final String FLASH_TYPE_KEY = "flashType";

    public static Javalin getApp() throws Exception {
        var dataSource = DataSourceFactory.getDataSource();
        DatabaseInitializer.init(dataSource);
        BaseRepository.setDataSource(dataSource);

        return Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.fileRenderer(new JavalinJte(createTemplateEngine()));

            config.routes.get(NamedRoutes.rootPath(), ctx -> {
                var model = baseModel(ctx);
                model.put("url", "");
                ctx.render("index.jte", model);
            });

            config.routes.post(NamedRoutes.urlsPath(), ctx -> {
                var inputUrl = ctx.formParam("url");
                var normalizedUrl = normalizeUrl(inputUrl);

                if (normalizedUrl == null) {
                    ctx.status(422);
                    var model = baseModel(ctx);
                    model.put("url", inputUrl == null ? "" : inputUrl);
                    model.put("flash", "Некорректный URL");
                    model.put("flashType", "danger");
                    ctx.render("index.jte", model);
                    return;
                }

                var existingUrl = UrlRepository.findByName(normalizedUrl);

                if (existingUrl.isPresent()) {
                    setFlash(ctx, "Страница уже существует", "info");
                    ctx.redirect(NamedRoutes.urlPath(existingUrl.get().getId()));
                    return;
                }

                var url = new Url(normalizedUrl);
                UrlRepository.save(url);
                setFlash(ctx, "Страница успешно добавлена", "success");
                ctx.redirect(NamedRoutes.urlPath(url.getId()));
            });

            config.routes.get(NamedRoutes.urlsPath(), ctx -> {
                var model = baseModel(ctx);
                model.put("urls", UrlRepository.getEntities());
                ctx.render("urls/index.jte", model);
            });

            config.routes.get("/urls/{id}", ctx -> {
                var id = ctx.pathParamAsClass("id", Long.class).get();
                var url = UrlRepository.find(id).
                        orElseThrow(() -> new NotFoundResponse("Page not found"));

                var model = baseModel(ctx);
                model.put("url", url);
                ctx.render("urls/show.jte", model);
            });
        });
    }

    public static void main(String[] args) throws Exception {
        var app = getApp();
        app.start(DEFAULT_HOST, getPort());
    }

    private static TemplateEngine createTemplateEngine() {
        var classLoader = App.class.getClassLoader();
        var codeResolver = new ResourceCodeResolver("templates", classLoader);
        return TemplateEngine.create(codeResolver, ContentType.Html);
    }

    private static int getPort() {
        var port = System.getenv("PORT");
        return port == null ? DEFAULT_PORT : Integer.parseInt(port);
    }

    private static String normalizeUrl(String rawUrl) {
        if (rawUrl == null || rawUrl.isBlank()) {
            return null;
        }

        try {
            URL url = new URI(rawUrl.trim()).toURL();
            var protocol = url.getProtocol();
            var host = url.getHost();
            var port = url.getPort();

            if (protocol == null || protocol.isBlank() || host == null || host.isBlank()) {
                return null;
            }

            return port == -1
                    ? String.format("%s://%s", protocol, host)
                    : String.format("%s://%s:%d", protocol, host, port);
        } catch (Exception e) {
            return null;
        }
    }

    private static void setFlash(io.javalin.http.Context ctx, String message, String type) {
        ctx.sessionAttribute(FLASH_KEY, message);
        ctx.sessionAttribute(FLASH_TYPE_KEY, type);
    }

    private static Map<String, Object> baseModel(io.javalin.http.Context ctx) {
        var model = new HashMap<String, Object>();
        model.put("flash", popSessionAttribute(ctx, FLASH_KEY));
        model.put("flashType", popSessionAttribute(ctx, FLASH_TYPE_KEY));
        return model;
    }

    private static String popSessionAttribute(io.javalin.http.Context ctx, String key) {
        String value = ctx.sessionAttribute(key);
        if (value != null) {
            ctx.req().getSession().removeAttribute(key);
        }
        return value;
    }
}
