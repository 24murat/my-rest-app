package com.fatykhov.restapp.repository.interfaces;

import java.util.List;

public interface BaseRepository<T> {
    List<T> findAll();

    T findOne(long id);

    T update(long id, T updatedElement);

    boolean remove(long id);
}
