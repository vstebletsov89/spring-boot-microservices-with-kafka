package ru.otus.hw.services;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.otus.hw.dto.GenreDto;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.mappers.GenreMapper;
import ru.otus.hw.mappers.GenreMapperImpl;
import ru.otus.hw.models.Genre;
import ru.otus.hw.repositories.GenreRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@DisplayName("Проверка работы сервиса жанров")
@SpringBootTest(classes = {GenreServiceImpl.class, GenreMapperImpl.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenreServiceImplTest {
    private static List<Genre> expectedGenres = new ArrayList<>();

    private static List<GenreDto> expectedGenresDto = new ArrayList<>();

    @MockBean
    private GenreRepository genreRepository;

    @Autowired
    private GenreService genreService;

    @Autowired
    private GenreMapper genreMapper;

    @BeforeAll
    void setExpectedGenres() {
        expectedGenres = List.of(
                new Genre(1L, "TestGenre1"),
                new Genre(2L, "TestGenre2"),
                new Genre(3L, "TestGenre3")
        );
        expectedGenresDto =
                expectedGenres.stream()
                        .map(genreMapper::toDto)
                        .toList();
    }

    @DisplayName("должен загружать список всех жанров")
    @Test
    void shouldReturnCorrectGenresList() {
        doReturn(expectedGenres).when(genreRepository).findAll();
        var actualGenres = genreService.findAll();

        assertEquals(3, actualGenres.size());
        assertEquals(expectedGenresDto, actualGenres);
    }

    @DisplayName("должен загружать жанр по id")
    @Test
    void shouldReturnCorrectGenreById() {
        long genreId = 1L;
        int genrePos = 0;
        doReturn(Optional.of(expectedGenres.get(genrePos))).when(genreRepository).findById(genreId);
        var actualGenre = genreService.findById(genreId);

        assertThat(actualGenre)
                .usingRecursiveComparison()
                .isEqualTo(expectedGenresDto.get(genrePos));
    }

    @DisplayName("должен выбрасывать исключение для неверного id")
    @Test
    void shouldReturnExceptionForInvalidId() {
        doReturn(Optional.empty()).when(genreRepository).findById(99L);
        var exception = assertThrows(NotFoundException.class,
                () -> genreService.findById(99L));

        assertEquals("Genre with id 99 not found", exception.getMessage());
    }
}