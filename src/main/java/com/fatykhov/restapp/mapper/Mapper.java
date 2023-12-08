package com.fatykhov.restapp.mapper;

public interface Mapper<T, R> {
    R entityToDto(T entity);

    T dtoToEntity(R dto);
}
