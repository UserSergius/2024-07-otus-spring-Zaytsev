package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;
public class TestServiceImplTest {

    @Mock
    private IOService ioService;
    @Mock
    private QuestionDao questionDao;
    @InjectMocks
    private TestServiceImpl testService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void executeTestFor() {
        final var answer = new Answer("answer", true);
        final var question = new Question("question", List.of(answer));
        final List<Question> questionList = List.of(question);
        final var student = new Student("Sergey", "Zaytsev");

        Mockito.when(questionDao.findAll()).thenReturn(questionList);
        final var result = testService.executeTestFor(student);

        Assertions.assertNotNull(result);
        Assertions.assertEquals("Sergey", result.getStudent().firstName());
        Assertions.assertEquals(1, result.getRightAnswersCount());
        Assertions.assertEquals(questionList, result.getAnsweredQuestions());
    }
}
