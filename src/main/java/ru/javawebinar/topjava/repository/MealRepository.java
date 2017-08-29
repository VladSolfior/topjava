package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface MealRepository {
    Meal save(Meal Meal, int userId);

    /* Spring repository get SQL count { with id} */
    boolean delete(int id, int userId);

    Meal get(int id, int userId);

    Collection<Meal> getAll(int userId);

    public List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId);
}
