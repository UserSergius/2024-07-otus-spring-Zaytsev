package ru.otus.hw03.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw03.dao.QuestionDao;
import ru.otus.hw03.domain.Answer;
import ru.otus.hw03.domain.Question;
import ru.otus.hw03.domain.Student;
import ru.otus.hw03.domain.TestResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        var questions = questionDao.findAll();

        return getTestResult(questions, student);
    }

    private void outputAnswer(List<Answer> answers) {
        for (int i = 0; i < answers.size(); i++) {
            ioService.printFormattedLineLocalized("TestService.choice.number", i, answers.get(i).text());
        }
    }

    private int getNumberAnswer(int countAnswer) {
        return ioService.readIntForRangeWithPromptLocalized(0,
                countAnswer,
                "TestService.enter.response.number",
                "TestService.invalid.input.format");
    }

    private boolean isAnswerValid(List<Answer> listAnswers, int numberAnswer) {
        return listAnswers.get(numberAnswer).isCorrect();
    }

    private TestResult getTestResult(List<Question> questions, Student student) {
        var testResult = new TestResult(student);
        for (var question: questions) {
            var listAnswers = question.answers();
            ioService.printLine(question.text() + "\n");
            outputAnswer(listAnswers);
            var answerNumber = getNumberAnswer(listAnswers.size());
            testResult.applyAnswer(question, isAnswerValid(listAnswers, answerNumber));
        }
        return testResult;
    }
}
