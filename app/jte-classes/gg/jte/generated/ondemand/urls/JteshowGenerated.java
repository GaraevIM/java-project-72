package gg.jte.generated.ondemand.urls;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.util.StringUtils;
@SuppressWarnings("unchecked")
public final class JteshowGenerated {
	public static final String JTE_NAME = "urls/show.jte";
	public static final int[] JTE_LINE_INFO = {0,0,1,2,3,3,3,3,8,8,13,13,14,14,14,20,20,20,24,24,24,28,28,28,34,34,34,34,53,53,55,55,55,56,56,56,57,57,57,58,58,58,59,59,59,60,60,60,62,62,65,65,65,66,66,66,3,4,5,6,6,6,6};
	public static void render(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, Url url, java.util.List<UrlCheck> checks, String flash, String flashType) {
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
				jteOutput.writeContent("/checks\">\r\n                <input type=\"submit\" class=\"btn btn-primary\" value=\"Запустить проверку\">\r\n            </form>\r\n        </div>\r\n\r\n        <h2 class=\"mb-3\">Проверки</h2>\r\n\r\n        <table class=\"table table-bordered table-hover\" data-test=\"checks\">\r\n            <thead>\r\n                <tr>\r\n                    <th>ID</th>\r\n                    <th>Код ответа</th>\r\n                    <th>h1</th>\r\n                    <th>title</th>\r\n                    <th>description</th>\r\n                    <th>Дата создания</th>\r\n                </tr>\r\n            </thead>\r\n            <tbody>\r\n                ");
				for (UrlCheck check : checks) {
					jteOutput.writeContent("\r\n                    <tr>\r\n                        <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(check.getId());
					jteOutput.writeContent("</td>\r\n                        <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(check.getStatusCode());
					jteOutput.writeContent("</td>\r\n                        <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(StringUtils.truncate(check.getH1()));
					jteOutput.writeContent("</td>\r\n                        <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(StringUtils.truncate(check.getTitle()));
					jteOutput.writeContent("</td>\r\n                        <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(StringUtils.truncate(check.getDescription()));
					jteOutput.writeContent("</td>\r\n                        <td>");
					jteOutput.setContext("td", null);
					jteOutput.writeUserContent(check.getCreatedAt().toLocalDateTime().toLocalDate().toString());
					jteOutput.writeContent("</td>\r\n                    </tr>\r\n                ");
				}
				jteOutput.writeContent("\r\n            </tbody>\r\n        </table>\r\n    ");
			}
		}, flash, flashType);
		jteOutput.writeContent("\r\n");
	}
	public static void renderMap(gg.jte.html.HtmlTemplateOutput jteOutput, gg.jte.html.HtmlInterceptor jteHtmlInterceptor, java.util.Map<String, Object> params) {
		Url url = (Url)params.get("url");
		java.util.List<UrlCheck> checks = (java.util.List<UrlCheck>)params.get("checks");
		String flash = (String)params.getOrDefault("flash", null);
		String flashType = (String)params.getOrDefault("flashType", null);
		render(jteOutput, jteHtmlInterceptor, url, checks, flash, flashType);
	}
}
