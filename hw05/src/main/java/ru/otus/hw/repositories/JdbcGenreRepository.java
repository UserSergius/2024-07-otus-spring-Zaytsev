package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcGenreRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Genre> findAll() {
        return jdbcTemplate.query("SELECT id, name FROM genres", new GenreRowMapper()) ;
    }

    @Override
    public Optional<Genre> findById(long id) {
        String sql = "SELECT id, name FROM genres WHERE id = :id";

        List<Genre> genres = jdbcTemplate.query(sql, Map.of("id", id), new GenreRowMapper());

        return genres.stream().findFirst();
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            long id = rs.getLong("id");
            String name = rs.getString("name");
            return new Genre(id, name);
        }
    }
}
