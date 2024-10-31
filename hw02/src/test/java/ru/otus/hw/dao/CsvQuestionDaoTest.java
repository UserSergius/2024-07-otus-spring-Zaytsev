package ru.otus.hw.dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.exceptions.QuestionReadException;


public class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider provider;

    @InjectMocks
    private CsvQuestionDao dao;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_notNull() {
        final var path = "questions.csv";

        Mockito.when(provider.getTestFileName()).thenReturn(path);
        final var result = dao.findAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(6, result.size());
    }

    @Test
    void findAll_error() {
        final var path = "src.question.csv";
        final var expectedException = "An error occurred while reading the file with the questions.";

        Mockito.when(provider.getTestFileName()).thenReturn(path);
        final var exception = Assertions.assertThrows(QuestionReadException.class, () -> dao.findAll());

        Assertions.assertEquals(expectedException, exception.getMessage());
    }
}
