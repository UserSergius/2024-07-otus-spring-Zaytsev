package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {

        File resource = getResource();
        List<QuestionDto> csv = null;
        try {
            csv = new CsvToBeanBuilder<QuestionDto>(new FileReader(resource))
                    .withProfile("questions")
                    .withSeparator(';')
                    .withType(QuestionDto.class)
                    .withSkipLines(1)
                    .build()
                    .parse();
        } catch (FileNotFoundException e) {
            throw new QuestionReadException("An error occurred while reading the file with the questions.");
        }

        return csv.stream().map(QuestionDto::toDomainObject).collect(Collectors.toList());
    }

    private File getResource() {
        var path = fileNameProvider.getTestFileName();
        var url = getClass().getClassLoader().getResource(path);
        File file;
        try {
            file = new File(Objects.requireNonNull(url).toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return file;
    }
}

