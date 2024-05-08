package ru.semenov.calendar;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;

@SpringBootApplication
public class CalendarApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(CalendarApplication.class)
				.beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
				.run(args);
	}
}
