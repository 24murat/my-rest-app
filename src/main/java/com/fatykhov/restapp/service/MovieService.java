package com.fatykhov.restapp.service;

import com.fatykhov.restapp.dto.MovieDto;
import com.fatykhov.restapp.entity.Movie;
import com.fatykhov.restapp.mapper.Mapper;
import com.fatykhov.restapp.mapper.MovieMapper;
import com.fatykhov.restapp.repository.MovieRepository;
import com.fatykhov.restapp.service.interfaces.MovieServiceInterface;

import java.util.List;

public class MovieService implements MovieServiceInterface {
    private final MovieRepository movieRepository;
    private final Mapper<Movie, MovieDto> movieMapper;

    public MovieService() {
        this.movieRepository = new MovieRepository();
        this.movieMapper = new MovieMapper();
    }

    public MovieService(MovieRepository movieRepository, Mapper<Movie, MovieDto> movieMapper) {
        this.movieRepository = movieRepository;
        this.movieMapper = movieMapper;
    }

    @Override
    public List<MovieDto> getAll() {
        List<Movie> movieList = movieRepository.findAll();
        return movieList.stream()
                .map(movieMapper::toDto)
                .toList();
    }

    @Override
    public MovieDto getById(long id) {
        return movieMapper.toDto(movieRepository.findOne(id));
    }

    public MovieDto save(MovieDto movieDto, List<Long> actorsId) {
        Movie movie = movieRepository.save(movieMapper.fromDto(movieDto), actorsId);
        return movieMapper.toDto(movie);
    }

    @Override
    public MovieDto update(long id, MovieDto updatedMovieDto) {
        Movie movie = movieRepository.update(id, movieMapper.fromDto(updatedMovieDto));
        return movieMapper.toDto(movie);
    }

    @Override
    public boolean remove(long id) {
        return movieRepository.remove(id);
    }
}
