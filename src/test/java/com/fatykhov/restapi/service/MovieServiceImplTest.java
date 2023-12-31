package com.fatykhov.restapi.service;

import com.fatykhov.restapi.util.TestObjectInitializer;
import com.fatykhov.restapp.dto.MovieDto;
import com.fatykhov.restapp.entity.Movie;
import com.fatykhov.restapp.mapper.MovieMapper;
import com.fatykhov.restapp.repository.impl.MovieRepositoryImpl;
import com.fatykhov.restapp.service.impl.MovieServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieServiceImplTest {

    @Mock
    private MovieRepositoryImpl movieRepositoryImpl;
    @Mock
    private MovieMapper movieMapper;
    @InjectMocks
    private MovieServiceImpl movieServiceImpl;
    @Spy
    private MovieServiceImpl spyService;

    private MovieDto expectedMovieDto;
    private Movie expectedMovie;

    @BeforeEach
    void setUp() {
        movieServiceImpl = new MovieServiceImpl(movieRepositoryImpl, movieMapper);
        spyService = spy(movieServiceImpl);

        expectedMovieDto = TestObjectInitializer.initializeMovieDto();
        expectedMovie = TestObjectInitializer.initializeMovie();
    }

    @Test
    void getAllTest() {
        when(movieRepositoryImpl.findAll()).thenReturn(List.of(expectedMovie));
        when(movieMapper.toDto(expectedMovie)).thenReturn(expectedMovieDto);

        List<MovieDto> movies = spyService.getAll();

        verify(movieMapper).toDto(expectedMovie);
        assertEquals(List.of(expectedMovieDto), movies);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L})
    void getByIdTest(long id) {
        when(movieRepositoryImpl.findOne(id)).thenReturn(expectedMovie);
        when(spyService.getById(id)).thenReturn(expectedMovieDto);

        MovieDto movieDto = spyService.getById(id);

        verify(movieMapper).toDto(expectedMovie);
        assertEquals(expectedMovieDto, movieDto);
    }

    @Test
    void saveTest() {
        List<Long> actorsId = List.of(1L);
        when(movieMapper.fromDto(expectedMovieDto)).thenReturn(expectedMovie);
        when(movieRepositoryImpl.save(expectedMovie, actorsId)).thenReturn(expectedMovie);
        when(movieMapper.toDto(expectedMovie)).thenReturn(expectedMovieDto);

        MovieDto savedMovieDto = spyService.save(expectedMovieDto, actorsId);

        verify(movieRepositoryImpl).save(expectedMovie, actorsId);
        verify(movieMapper).toDto(expectedMovie);
        verify(movieMapper).fromDto(expectedMovieDto);
        assertEquals(expectedMovieDto, savedMovieDto);
    }

    @Test
    void updateTest() {
        when(movieMapper.toDto(expectedMovie)).thenReturn(expectedMovieDto);
        when(movieMapper.fromDto(expectedMovieDto)).thenReturn(expectedMovie);
        when(movieRepositoryImpl.update(eq(1L), eq(expectedMovie))).thenReturn(expectedMovie);

        MovieDto result = spyService.update(1L, expectedMovieDto);

        verify(movieMapper).toDto(expectedMovie);
        verify(movieMapper).fromDto(expectedMovieDto);
        verify(movieRepositoryImpl).update(eq(1L), eq(expectedMovie));
        assertEquals(expectedMovieDto, result);
    }

    @Test
    void removeTest() {
        when(movieRepositoryImpl.remove(1L)).thenReturn(true);

        boolean result = spyService.remove(1L);

        assertTrue(result);
    }
}
