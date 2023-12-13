package com.fatykhov.restapp.repository.interfaces;

import java.util.List;

public interface BaseRepository<T> {
    List<T> findAll();

    T findOne(Long id);

    T update(Long id, T updatedElement);

    boolean remove(Long id);
}
