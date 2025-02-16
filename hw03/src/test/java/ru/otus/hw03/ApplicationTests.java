package ru.otus.hw03;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import ru.otus.hw03.config.AppProperties;

@SpringBootTest
@RequiredArgsConstructor
class ApplicationTests {


	private final ApplicationContext applicationContext;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(applicationContext.getBean(AppProperties.class));
	}

}
