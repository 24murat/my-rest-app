package com.fatykhov.restapp.service.impl;

import com.fatykhov.restapp.dto.MovieDto;
import com.fatykhov.restapp.entity.Movie;
import com.fatykhov.restapp.mapper.Mapper;
import com.fatykhov.restapp.mapper.MovieMapper;
import com.fatykhov.restapp.repository.impl.MovieRepositoryImpl;
import com.fatykhov.restapp.service.MovieService;

import java.util.List;

public class MovieServiceImpl implements MovieService {
    private final MovieRepositoryImpl movieRepositoryImpl;
    private final Mapper<Movie, MovieDto> movieMapper;

    public MovieServiceImpl() {
        this.movieRepositoryImpl = new MovieRepositoryImpl();
        this.movieMapper = new MovieMapper();
    }

    public MovieServiceImpl(MovieRepositoryImpl movieRepositoryImpl, Mapper<Movie, MovieDto> movieMapper) {
        this.movieRepositoryImpl = movieRepositoryImpl;
        this.movieMapper = movieMapper;
    }

    @Override
    public List<MovieDto> getAll() {
        List<Movie> movieList = movieRepositoryImpl.findAll();
        return movieList.stream()
                .map(movieMapper::toDto)
                .toList();
    }

    @Override
    public MovieDto getById(long id) {
        return movieMapper.toDto(movieRepositoryImpl.findOne(id));
    }

    public MovieDto save(MovieDto movieDto, List<Long> actorsId) {
        Movie movie = movieRepositoryImpl.save(movieMapper.fromDto(movieDto), actorsId);
        return movieMapper.toDto(movie);
    }

    @Override
    public MovieDto update(long id, MovieDto updatedMovieDto) {
        Movie movie = movieRepositoryImpl.update(id, movieMapper.fromDto(updatedMovieDto));
        return movieMapper.toDto(movie);
    }

    @Override
    public boolean remove(long id) {
        return movieRepositoryImpl.remove(id);
    }
}
