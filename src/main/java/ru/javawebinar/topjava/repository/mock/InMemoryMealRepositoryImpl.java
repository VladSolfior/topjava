package ru.javawebinar.topjava.repository.mock;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();

    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach( meal -> this.save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> meals = repository.computeIfAbsent(userId, ConcurrentHashMap::new);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            return meals.put(meal.getId(), meal);
        }

        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);

    }
    // false if not found
    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null && meals.remove(id) != null;
    }
    // null if not found
    @Override
    public Meal get(int id, int userId) {
        return repository.get(userId).values().stream().filter(meal -> meal.getId().equals(id)).findFirst().orElse(null);
    }

    public List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        List<Meal> res = repository.get(userId).values()
                .stream()
                .filter(meal -> DateTimeUtil.isBetween(meal.getDateTime().toLocalTime(), startDateTime.toLocalTime(), endDateTime.toLocalTime()))
                .sorted(Comparator.comparing(Meal::getDate).
                        thenComparing(Meal::getTime).reversed())
                .collect(Collectors.toList());
        return res != null ? res : Collections.emptyList();
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.get(userId).values()
                .stream()
                .sorted(Comparator.comparing(Meal::getDate).
                        thenComparing(Meal::getTime).reversed())
                .collect(Collectors.toList());
    }

//    public static void main(String[] args) {
//        InMemoryMealRepositoryImpl repository = new InMemoryMealRepositoryImpl();
//
//        Collection<Meal> all = repository.getAll(AuthorizedUser.id());
//        all.forEach(meal -> System.out.println(meal.toString()));
//
//        Meal m = new Meal(LocalDateTime.of(2017, Month.AUGUST, 23, 12, 30), "перекус", 500);
//        repository.save(m, 2);
//
//        Meal m2 = repository.get(4, 2);
//        System.out.println(m2.toString());
//    }
}

