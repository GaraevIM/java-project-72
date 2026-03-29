@import hexlet.code.model.Url
@param Url url
@param String flash = null
@param String flashType = null

@template.layout.main(
    pageTitle = "Сайт",
    flash = flash,
    flashType = flashType,
    content = @`
        <h1 class="mb-5">Сайт: ${url.getName()}</h1>
        <table class="table table-bordered table-hover" data-test="url">
            <tbody>
                <tr>
                    <td>ID</td>
                    <td>${url.getId()}</td>
                </tr>
                <tr>
                    <td>Имя</td>
                    <td>${url.getName()}</td>
                </tr>
                <tr>
                    <td>Дата создания</td>
                    <td>${url.getCreatedAt().toLocalDateTime().toLocalDate()}</td>
                </tr>
            </tbody>
        </table>
    `
)
