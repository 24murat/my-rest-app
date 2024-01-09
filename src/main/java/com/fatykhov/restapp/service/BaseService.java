package com.fatykhov.restapp.service;

import java.util.List;

public interface BaseService<T> {
    List<T> getAll();

    T getById(long id);

    T update(long id, T updatedElement);

    boolean remove(long id);
}
