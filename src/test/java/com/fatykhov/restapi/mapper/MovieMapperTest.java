package com.fatykhov.restapi.mapper;

import com.fatykhov.restapp.dto.MovieDto;
import com.fatykhov.restapp.entity.Movie;
import com.fatykhov.restapp.mapper.MovieMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieMapperTest {

    @Mock
    private MovieMapper movieMapper;

    private MovieDto expectedMovieDto;
    private Movie expectedMovie;

    @BeforeEach
    void setUp() {
        expectedMovieDto = MovieDto.builder()
                .id(1L)
                .clientId(2L)
                .title("TestMovie")
                .build();

        expectedMovie = Movie.builder()
                .id(1L)
                .clientId(2L)
                .title("TestMovie")
                .build();
    }

    @Test
    void toDtoTest() {
        when(movieMapper.toDto(expectedMovie)).thenReturn(expectedMovieDto);
        MovieDto movieDto = movieMapper.toDto(expectedMovie);
        assertEquals(expectedMovieDto, movieDto);
    }

    @Test
    void fromDtoTest() {
        when(movieMapper.fromDto(expectedMovieDto)).thenReturn(expectedMovie);
        Movie movie = movieMapper.fromDto(expectedMovieDto);
        assertEquals(expectedMovie, movie);
    }
}

