package com.sample.telegrambot.cache;

import java.util.Collection;

/**
 * Temporal storage for user info.
 * TODO Replace with repository
 */
public interface Cache<T> {
  void add(T data);

  void remove(T t);

  T findById(Long id);

  Collection<T> getAll();
}
