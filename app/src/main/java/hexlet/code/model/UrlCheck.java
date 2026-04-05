package hexlet.code.model;

import java.sql.Timestamp;

public final class UrlCheck {
    private Long id;

    private Long urlId;

    private Integer statusCode;

    private String h1;

    private String title;

    private String description;

    private Timestamp createdAt;

    public UrlCheck(Long urlId, Integer statusCode, String h1, String title, String description) {
        this.urlId = urlId;
        this.statusCode = statusCode;
        this.h1 = h1;
        this.title = title;
        this.description = description;
    }

    public UrlCheck(Long id, Long urlId, Integer statusCode, String h1,
                    String title, String description, Timestamp createdAt) {
        this.id = id;
        this.urlId = urlId;
        this.statusCode = statusCode;
        this.h1 = h1;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public Long getUrlId() {
        return urlId;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getH1() {
        return h1;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUrlId(Long urlId) {
        this.urlId = urlId;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public void setH1(String h1) {
        this.h1 = h1;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
