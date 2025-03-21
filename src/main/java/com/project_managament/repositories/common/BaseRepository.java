package com.project_managament.repositories.common;

import java.util.List;

public interface BaseRepository<T> {
    int insert(T t);
    boolean update(T t);
    boolean delete(int id);
    T getById(int id);
    List<T> getAll();
}
