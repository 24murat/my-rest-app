package com.fatykhov.restapp.mapper;

public interface Mapper<T, R> {
    R toDto(T entity);

    T fromDto(R dto);
}
