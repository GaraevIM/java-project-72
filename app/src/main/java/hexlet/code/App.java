package hexlet.code;

import hexlet.code.model.Url;
import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.UrlRepository;
import hexlet.code.util.DataSourceFactory;
import hexlet.code.util.DatabaseInitializer;
import hexlet.code.util.NamedRoutes;
import io.javalin.Javalin;

public class App {
    private static final int DEFAULT_PORT = 7070;

    private static final String DEFAULT_HOST = "0.0.0.0";

    public static Javalin getApp() throws Exception {
        var dataSource = DataSourceFactory.getDataSource();
        DatabaseInitializer.init(dataSource);
        BaseRepository.setDataSource(dataSource);

        return Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.routes.get(NamedRoutes.rootPath(), ctx -> {
                var html = """
                    <!doctype html>
                    <html lang="en">
                    <head>
                        <meta charset="UTF-8">
                        <meta name="viewport" content="width=device-width, initial-scale=1.0">
                        <title>Page Analyzer</title>
                    </head>
                    <body>
                        <h1>Page Analyzer</h1>
                        <form action="/urls" method="post">
                            <label for="url">URL</label>
                            <input id="url" name="url" type="text" placeholder="https://example.com">
                            <button type="submit">Check</button>
                        </form>
                    </body>
                    </html>
                    """;
                ctx.html(html);
            });

            config.routes.post(NamedRoutes.urlsPath(), ctx -> {
                var name = ctx.formParam("url");
                var url = new Url(name);
                UrlRepository.save(url);
                ctx.result("URL saved");
            });
        });
    }

    public static void main(String[] args) throws Exception {
        var app = getApp();
        app.start(DEFAULT_HOST, getPort());
    }

    private static int getPort() {
        var port = System.getenv("PORT");
        return port == null ? DEFAULT_PORT : Integer.parseInt(port);
    }
}
