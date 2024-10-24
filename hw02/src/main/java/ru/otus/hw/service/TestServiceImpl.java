package ru.otus.hw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

import java.util.List;

@Service
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    @Autowired
    public TestServiceImpl(IOService ioService, QuestionDao questionDao) {
        this.ioService = ioService;
        this.questionDao = questionDao;
    }

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");
        var questions = questionDao.findAll();
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

    private void outputAnswer(List<Answer> answers) {
        for (int i = 0; i < answers.size(); i++) {
            ioService.printLine("Choice number " +  i + ": " + answers.get(i).text());
        }
    }

    private int getNumberAnswer(int countAnswer) {
        return ioService.readIntForRangeWithPrompt(0,
                countAnswer,
                "\nEnter response number ",
                "Invalid input format, please try again");
    }

    private boolean isAnswerValid(List<Answer> listAnswers, int numberAnswer) {
        return listAnswers.get(numberAnswer).isCorrect();
    }


}
