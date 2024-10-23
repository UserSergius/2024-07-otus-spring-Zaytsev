package ru.otus.hw.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

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
            var isAnswerValid = false;
            var countAnswer = question.answers();
            ioService.printLine(question.text() + "\n");

            for (int i = 0; i < countAnswer.size(); i++) {
                ioService.printLine("Choice number " +  i + ": " + question.answers().get(i).text());
                }
            var numberResponse = ioService.readIntForRangeWithPrompt(0,
                    countAnswer.size(),
                    "\nEnter response number ",
                    "Invalid input format, please try again");
            isAnswerValid = countAnswer.get(numberResponse).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }
}
