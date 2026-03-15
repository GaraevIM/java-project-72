package hexlet.code;

import io.javalin.Javalin;

public class App {
    private static final int DEFAULT_PORT = 7070;

    private static final String DEFAULT_HOST = "0.0.0.0";

    public static Javalin getApp() {
        return Javalin.create(config -> {
            config.bundledPlugins.enableDevLogging();
            config.routes.get("/", ctx -> ctx.result("Hello World"));
        });
    }

    public static void main(String[] args) {
        var app = getApp();
        app.start(DEFAULT_HOST, getPort());
    }

    private static int getPort() {
        var port = System.getenv("PORT");
        return port == null ? DEFAULT_PORT : Integer.parseInt(port);
    }
}
