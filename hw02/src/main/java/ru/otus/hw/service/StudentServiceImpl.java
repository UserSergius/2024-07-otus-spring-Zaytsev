package ru.otus.hw.service;

import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Student;


@Service
public class StudentServiceImpl implements StudentService {

    private final IOService ioService;

    public StudentServiceImpl(IOService ioService) {
        this.ioService = ioService;
    }


    @Override
    public Student determineCurrentStudent() {
        var firstName = ioService.readStringWithPrompt("Please input your first name");
        var lastName = ioService.readStringWithPrompt("Please input your last name");
        return new Student(firstName, lastName);
    }
}
