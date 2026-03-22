package hexlet.code.repository;

import hexlet.code.model.Url;

import java.sql.Timestamp;
import java.util.Optional;

public class UrlRepository extends BaseRepository {
    public static void save(Url url) throws Exception {
        var sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";

        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, url.getName());
            statement.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
            statement.executeUpdate();

            try (var generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    url.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    public static Optional<Url> find(Long id) throws Exception {
        var sql = "SELECT * FROM urls WHERE id = ?";

        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement(sql)
        ) {
            statement.setLong(1, id);

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    var url = new Url(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getTimestamp("created_at")
                    );
                    return Optional.of(url);
                }
            }
        }

        return Optional.empty();
    }

    public static Optional<Url> findByName(String name) throws Exception {
        var sql = "SELECT * FROM urls WHERE name = ?";

        try (
                var connection = dataSource.getConnection();
                var statement = connection.prepareStatement(sql)
        ) {
            statement.setString(1, name);

            try (var resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    var url = new Url(
                            resultSet.getLong("id"),
                            resultSet.getString("name"),
                            resultSet.getTimestamp("created_at")
                    );
                    return Optional.of(url);
                }
            }
        }

        return Optional.empty();
    }
}
