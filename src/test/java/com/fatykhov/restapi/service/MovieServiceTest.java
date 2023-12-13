package com.fatykhov.restapi.service;

import com.fatykhov.restapp.dto.MovieDto;
import com.fatykhov.restapp.entity.Movie;
import com.fatykhov.restapp.mapper.MovieMapper;
import com.fatykhov.restapp.repository.MovieRepository;
import com.fatykhov.restapp.service.MovieService;
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
public class MovieServiceTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private MovieMapper movieMapper;
    @InjectMocks
    private MovieService movieService;
    @Spy
    private MovieService spyService;

    private MovieDto expectedMovieDto;
    private Movie expectedMovie;

    @BeforeEach
    void setUp() {
        movieService = new MovieService(movieRepository, movieMapper);
        spyService = spy(movieService);

        expectedMovieDto = MovieDto.builder()
                .id(1)
                .title("TestMovie")
                .build();

        expectedMovie = Movie.builder()
                .id(1)
                .title("TestMovie")
                .build();
    }

    @Test
    void getAllMoviesTest() {
        when(movieRepository.findAll()).thenReturn(List.of(expectedMovie));
        when(movieMapper.entityToDto(expectedMovie)).thenReturn(expectedMovieDto);

        List<MovieDto> movies = spyService.getAllMovies();

        verify(movieMapper).entityToDto(expectedMovie);
        assertEquals(List.of(expectedMovieDto), movies);
    }

    @ParameterizedTest
    @ValueSource(longs = {1L, 2L, 3L, 4L, 5L, 6L, 7L, 8L})
    void getMovieByIdTest(long id) {
        when(movieRepository.findOne(id)).thenReturn(expectedMovie);
        when(spyService.getMovieById(id)).thenReturn(expectedMovieDto);

        MovieDto movieDto = spyService.getMovieById(id);

        verify(movieMapper).entityToDto(expectedMovie);
        assertEquals(expectedMovieDto, movieDto);
    }

    @Test
    void saveMovieTest() {
        List<Long> actorsId = List.of(1L);
        when(movieMapper.dtoToEntity(expectedMovieDto)).thenReturn(expectedMovie);
        when(movieRepository.save(expectedMovie, actorsId)).thenReturn(expectedMovie);
        when(movieMapper.entityToDto(expectedMovie)).thenReturn(expectedMovieDto);

        MovieDto savedMovieDto = spyService.saveMovie(expectedMovieDto, actorsId);

        verify(movieRepository).save(expectedMovie, actorsId);
        verify(movieMapper).entityToDto(expectedMovie);
        verify(movieMapper).dtoToEntity(expectedMovieDto);
        assertEquals(expectedMovieDto, savedMovieDto);
    }

    @Test
    void updateMovieTest() {
        when(movieMapper.entityToDto(expectedMovie)).thenReturn(expectedMovieDto);
        when(movieMapper.dtoToEntity(expectedMovieDto)).thenReturn(expectedMovie);
        when(movieRepository.update(eq(1L), eq(expectedMovie))).thenReturn(expectedMovie);

        MovieDto result = spyService.updateMovie(1L, expectedMovieDto);

        verify(movieMapper).entityToDto(expectedMovie);
        verify(movieMapper).dtoToEntity(expectedMovieDto);
        verify(movieRepository).update(eq(1L), eq(expectedMovie));
        assertEquals(expectedMovieDto, result);
    }

    @Test
    void removeMovieTest() {
        when(movieRepository.remove(1L)).thenReturn(true);

        boolean result = spyService.removeMovie(1L);

        assertTrue(result);
    }
}
