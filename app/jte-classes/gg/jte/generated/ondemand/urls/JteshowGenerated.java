package gg.jte.generated.ondemand.urls;
import hexlet.code.model.Url;
@SuppressWarnings("unchecked")
public final class JteshowGenerated {
	public static final String JTE_NAME = "urls/show.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,1,1,1,5,5,10,10,11,11,11,17,17,17,21,21,21,25,25,25,31,31,31,31,52,52,52,53,53,53,1,2,3,3,3,3};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Url url, String flash, String flashType) {
		jteOutput.writeContent("\r\n");
		gg.jte.generated.ondemand.layout.JtemainGenerated.render(jteOutput, jteHtmlInterceptor, "Сайт", new gg.jte.html.HtmlContent() {
			public void writeTo(gg.jte.html.HtmlTemplateOutput jteOutput) {
				jteOutput.writeContent("\r\n        <h1 class=\"mb-5\">Сайт: ");
				jteOutput.setContext("h1", null);
				jteOutput.writeUserContent(url.getName());
				jteOutput.writeContent("</h1>\r\n\r\n        <table class=\"table table-bordered table-hover mb-5\" data-test=\"url\">\r\n            <tbody>\r\n                <tr>\r\n                    <td>ID</td>\r\n                    <td>");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(url.getId());
				jteOutput.writeContent("</td>\r\n                </tr>\r\n                <tr>\r\n                    <td>Имя</td>\r\n                    <td>");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(url.getName());
				jteOutput.writeContent("</td>\r\n                </tr>\r\n                <tr>\r\n                    <td>Дата создания</td>\r\n                    <td>");
				jteOutput.setContext("td", null);
				jteOutput.writeUserContent(url.getCreatedAt().toLocalDateTime().toLocalDate().toString());
				jteOutput.writeContent("</td>\r\n                </tr>\r\n            </tbody>\r\n        </table>\r\n\r\n        <div class=\"mb-5\">\r\n            <form method=\"post\" action=\"/urls/");
				jteOutput.setContext("form", "action");
				jteOutput.writeUserContent(url.getId());
				jteOutput.setContext("form", null);
				jteOutput.writeContent("/checks\">\r\n                <input type=\"submit\" class=\"btn btn-primary\" value=\"Запустить проверку\">\r\n            </form>\r\n        </div>\r\n\r\n        <h2 class=\"mb-3\">Проверки</h2>\r\n\r\n        <table class=\"table table-bordered table-hover\" data-test=\"checks\">\r\n            <thead>\r\n                <tr>\r\n                    <th>ID</th>\r\n                    <th>Код ответа</th>\r\n                    <th>h1</th>\r\n                    <th>title</th>\r\n                    <th>description</th>\r\n                    <th>Дата создания</th>\r\n                </tr>\r\n            </thead>\r\n            <tbody>\r\n            </tbody>\r\n        </table>\r\n    ");
			}
		}, flash, flashType);
		jteOutput.writeContent("\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Url url = (Url)params.get("url");
		String flash = (String)params.getOrDefault("flash", null);
		String flashType = (String)params.getOrDefault("flashType", null);
		render(jteOutput, jteHtmlInterceptor, url, flash, flashType);
	}
}
