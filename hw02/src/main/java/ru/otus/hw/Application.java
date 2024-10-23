package ru.otus.hw;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.service.TestRunnerService;

@ComponentScan(basePackages = "ru.otus.hw")
@Configuration
public class Application {
    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext("ru.otus.hw");
        var testRunnerService = context.getBean(TestRunnerService.class);
        testRunnerService.run();

    }
}