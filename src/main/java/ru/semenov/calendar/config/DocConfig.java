package ru.semenov.calendar.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocConfig {
    @Bean
    public OpenAPI projectOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("Календарь задач и событий рабочего коллектива")
                .description("Календарь поможет наладить работу сотрудников предприятия")
                .version("v0.0.1")
                .contact(new Contact()
                    .name("Михаил Семенов")
                    .url("https://github.com/m1hail1337")
                    .email("mihail7enov@mail.ru")
                ));
    }
}
