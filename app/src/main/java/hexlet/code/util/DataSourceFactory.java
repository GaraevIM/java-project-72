package hexlet.code.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class DataSourceFactory {
    private static final String DEFAULT_DB_URL = "jdbc:h2:mem:project;DB_CLOSE_DELAY=-1;";

    public static DataSource getDataSource() {
        var databaseUrl = System.getenv("JDBC_DATABASE_URL");
        var jdbcUrl = databaseUrl == null || databaseUrl.isBlank() ? DEFAULT_DB_URL : databaseUrl;

        var config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);

        if (jdbcUrl.startsWith("jdbc:postgresql:")) {
            config.setDriverClassName("org.postgresql.Driver");
        } else if (jdbcUrl.startsWith("jdbc:h2:")) {
            config.setDriverClassName("org.h2.Driver");
        }

        return new HikariDataSource(config);
    }
}
