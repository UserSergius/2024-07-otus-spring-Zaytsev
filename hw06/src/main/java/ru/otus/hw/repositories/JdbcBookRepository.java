package ru.otus.hw.repositories;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public JdbcBookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Book> findById(long id) {
        var sql = """
                SELECT b.id, b.title,
                             a.id AS author_id, a.full_name AS author_full_name,
                             g.id AS genre_id, g.name AS genre_name
                      FROM books b
                      LEFT JOIN authors a ON b.author_id = a.id
                      LEFT JOIN genres g ON b.genre_id = g.id
                      WHERE b.id = :id
                """;

        List<Book> books = jdbcTemplate.query(sql, Map.of("id", id), new BookRowMapper());

        return books.stream().findFirst();
    }

    @Override
    public List<Book> findAll() {
      return jdbcTemplate.query("""
                      SELECT b.id, b.title,
                             a.id AS author_id, a.full_name AS author_full_name,
                             g.id AS genre_id, g.name AS genre_name
                      FROM books b
                      LEFT JOIN authors a ON b.author_id = a.id
                      LEFT JOIN genres g ON b.genre_id = g.id;
                      """, new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        var sql = "DELETE FROM books WHERE id = :id";
        jdbcTemplate.update(sql, Map.of("id", id));
    }

    private Book insert(Book book) {
        var keyHolder = new GeneratedKeyHolder();
        var sql = "INSERT INTO books (title, author_id, genre_id) VALUES ( :title, :authorId, :genreId)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("authorId", book.getAuthor().getId())
                .addValue("genreId", book.getGenre().getId());

        jdbcTemplate.update(sql, params, keyHolder);

        book.setId(Objects.requireNonNull(keyHolder.getKeyAs(Long.class)));
        return book;
    }

    private Book update(Book book) {
        var sql = "UPDATE books SET title = :title, author_id = :authorId, genre_id = :genreId WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("title", book.getTitle())
                .addValue("authorId", book.getAuthor().getId())
                .addValue("genreId", book.getGenre().getId())
                .addValue("id", book.getId());

        int updatedRows = jdbcTemplate.update(sql, params);

        if (updatedRows == 0) {
            throw new EntityNotFoundException("Book with id=" + book.getId() + " not found");
        }
        return book;
    }

    private static class BookRowMapper implements RowMapper<Book> {

        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            long id = rs.getLong("id");
            String title = rs.getString("title");

            Long authorId = rs.getObject("author_id", Long.class);
            String authorName = rs.getString("author_full_name");
            Author author = (authorId != null) ? new Author(authorId, authorName) : null;

            Long genreId = rs.getObject("genre_id", Long.class);
            String genreName = rs.getString("genre_name");
            Genre genre = (genreId != null) ? new Genre(genreId, genreName) : null;

            return new Book(id, title, author, genre);
        }
    }
}
