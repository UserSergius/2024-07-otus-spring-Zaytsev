package dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.otus.hw.Application;
import ru.otus.hw.dao.CsvQuestionDao;


@TestPropertySource("classpath:questions.csv")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {Application.class})
public class CsvQuestionDaoTest {

    @Autowired
    private CsvQuestionDao dao;

    @Test
    void findAll() {

        var listQuestion = dao.findAll();

        Assertions.assertNotNull(listQuestion);
        Assertions.assertEquals(6, listQuestion.size());
    }
}
