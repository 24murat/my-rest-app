package com.fatykhov.restapp.mapper;

import com.fatykhov.restapp.dto.MovieDto;
import com.fatykhov.restapp.entity.Movie;

public class MovieMapper implements Mapper<Movie, MovieDto> {

    @Override
    public MovieDto entityToDto(Movie movie) {
        return MovieDto.builder()
                .id(movie.getId())
                .clientId(movie.getClientId())
                .title(movie.getTitle())
                .build();
    }

    @Override
    public Movie dtoToEntity(MovieDto movieDto) {
        return Movie.builder()
                .id(movieDto.getId())
                .clientId(movieDto.getClientId())
                .title(movieDto.getTitle())
                .build();
    }
}
