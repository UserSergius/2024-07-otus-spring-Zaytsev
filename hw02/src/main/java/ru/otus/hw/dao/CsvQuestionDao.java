package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {

        final var path = fileNameProvider.getTestFileName();

        try (InputStream resource = getClass().getClassLoader().getResourceAsStream(path)) {
            List<QuestionDto> csv = new CsvToBeanBuilder<QuestionDto>(
                    new InputStreamReader(Objects.requireNonNull(resource)))
                    .withProfile("questions")
                    .withSeparator(';')
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .build()
                    .parse();

          return csv.stream().map(QuestionDto::toDomainObject).collect(Collectors.toList());
        } catch (IOException | NullPointerException e) {
            throw new QuestionReadException("An error occurred while reading the file with the questions."
                    + e.getCause());
        }
    }
}

