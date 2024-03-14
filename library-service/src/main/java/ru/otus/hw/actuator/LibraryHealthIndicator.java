package ru.otus.hw.actuator;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.GenreService;

@RequiredArgsConstructor
@Component
public class LibraryHealthIndicator implements HealthIndicator {

    private final AuthorService authorService;

    private final GenreService genreService;

    @Override
    public Health health() {
        try {
            var authors = authorService.findAll();
            var genres = genreService.findAll();
            if (authors.isEmpty() || genres.isEmpty()) {
                return Health.down()
                        .status(Status.DOWN)
                        .withDetail("message", "Данные в базе повреждены!")
                        .build();
            } else {
                return Health.up().withDetail("message", "Сервис в порядке!").build();
            }
        } catch (Exception exception) {
            return Health.down()
                    .status(Status.DOWN)
                    .withDetail("message", "Ошибка обращения к базе: " + ExceptionUtils.getStackTrace(exception))
                    .build();
        }
    }
}