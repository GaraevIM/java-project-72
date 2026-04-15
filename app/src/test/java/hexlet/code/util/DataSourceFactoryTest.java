package hexlet.code.util;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DataSourceFactoryTest {

    @Test
    public void testGetDataSourceUsesPropertyValue() throws Exception {
        var previousValue = System.getProperty("JDBC_DATABASE_URL");

        try {
            System.setProperty("JDBC_DATABASE_URL", "jdbc:h2:mem:test_db_property;DB_CLOSE_DELAY=-1;");
            var dataSource = DataSourceFactory.getDataSource();

            Assertions.assertInstanceOf(HikariDataSource.class, dataSource);

            try (var connection = dataSource.getConnection()) {
                Assertions.assertFalse(connection.isClosed());
            }

            ((HikariDataSource) dataSource).close();
        } finally {
            restoreJdbcDatabaseUrl(previousValue);
        }
    }

    @Test
    public void testGetDataSourceUsesDefaultValueWhenPropertyIsBlank() throws Exception {
        var previousValue = System.getProperty("JDBC_DATABASE_URL");

        try {
            System.setProperty("JDBC_DATABASE_URL", "   ");
            var dataSource = DataSourceFactory.getDataSource();

            Assertions.assertInstanceOf(HikariDataSource.class, dataSource);

            try (var connection = dataSource.getConnection()) {
                Assertions.assertFalse(connection.isClosed());
            }

            ((HikariDataSource) dataSource).close();
        } finally {
            restoreJdbcDatabaseUrl(previousValue);
        }
    }

    private void restoreJdbcDatabaseUrl(String previousValue) {
        if (previousValue == null) {
            System.clearProperty("JDBC_DATABASE_URL");
        } else {
            System.setProperty("JDBC_DATABASE_URL", previousValue);
        }
    }
}
