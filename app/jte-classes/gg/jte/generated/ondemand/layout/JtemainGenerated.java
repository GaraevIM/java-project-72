package gg.jte.generated.ondemand.layout;
import gg.jte.Content;
@SuppressWarnings("unchecked")
public final class JtemainGenerated {
	public static final String JTE_NAME = "layout/main.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,1,11,11,11,11,25,25,28,28,28,28,29,29,29,33,33,34,34,34,39,39,39,1,2,3,4,4,4,4};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, String pageTitle, Content content, String flash, String flashType) {
		jteOutput.writeContent("\r\n<!doctype html>\r\n<html lang=\"ru\">\r\n<head>\r\n    <meta charset=\"UTF-8\">\r\n    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n    <title>");
		jteOutput.setContext("title", null);
		jteOutput.writeUserContent(pageTitle);
		jteOutput.writeContent("</title>\r\n    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css\" rel=\"stylesheet\">\r\n</head>\r\n<body>\r\n<nav class=\"navbar navbar-expand-lg navbar-light bg-white border-bottom mb-5\">\r\n    <div class=\"container\">\r\n        <a class=\"navbar-brand\" href=\"/\">Анализатор страниц</a>\r\n        <div class=\"navbar-nav\">\r\n            <a class=\"nav-link\" href=\"/\">Главная</a>\r\n            <a class=\"nav-link\" href=\"/urls\">Сайты</a>\r\n        </div>\r\n    </div>\r\n</nav>\r\n<main class=\"container\">\r\n    ");
		if (flash != null) {
			jteOutput.writeContent("\r\n        <div class=\"row mb-3\">\r\n            <div class=\"col-12\">\r\n                <div class=\"alert alert-");
			jteOutput.setContext("div", "class");
			jteOutput.writeUserContent(flashType);
			jteOutput.setContext("div", null);
			jteOutput.writeContent("\" role=\"alert\">\r\n                    ");
			jteOutput.setContext("div", null);
			jteOutput.writeUserContent(flash);
			jteOutput.writeContent("\r\n                </div>\r\n            </div>\r\n        </div>\r\n    ");
		}
		jteOutput.writeContent("\r\n    ");
		jteOutput.setContext("main", null);
		jteOutput.writeUserContent(content);
		jteOutput.writeContent("\r\n</main>\r\n<script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js\"></script>\r\n</body>\r\n</html>\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		String pageTitle = (String)params.get("pageTitle");
		Content content = (Content)params.get("content");
		String flash = (String)params.getOrDefault("flash", null);
		String flashType = (String)params.getOrDefault("flashType", null);
		render(jteOutput, jteHtmlInterceptor, pageTitle, content, flash, flashType);
	}
}
