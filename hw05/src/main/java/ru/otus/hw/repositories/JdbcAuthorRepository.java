package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcAuthorRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Author> findAll() {

        return jdbcTemplate.query("SELECT id, full_name FROM authors", new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        String sql = "SELECT id, full_name FROM authors WHERE id = :id";

        List<Author> authors = jdbcTemplate.query(sql, Map.of("id", id), new AuthorRowMapper());

        return authors.stream().findFirst();
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String fullName = rs.getString("full_name");
            return new Author(id, fullName);
        }
    }
}
