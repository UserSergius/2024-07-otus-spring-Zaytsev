package dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;

public class CsvQuestionDaoTest {

    @Mock
    private TestFileNameProvider provider;

    @Mock
    private CsvQuestionDao dao;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll() {
        final var path = "classpath:questions.csv";

        Mockito.when(provider.getTestFileName()).thenReturn(path);
        final var result = dao.findAll();
        Assertions.assertNotNull(result);
    }
}
