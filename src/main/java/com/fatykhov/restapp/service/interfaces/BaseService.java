package com.fatykhov.restapp.service.interfaces;

import java.util.List;

public interface BaseService<T> {
    List<T> getAll();

    T getById(long id);

    T update(long id, T updatedElement);

    boolean remove(long id);
}
