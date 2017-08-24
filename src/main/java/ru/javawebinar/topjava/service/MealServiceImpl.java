package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MealServiceImpl implements MealService {

    public static void main(String[] args) {
        MealServiceImpl thisService = new MealServiceImpl();
        thisService.repository = new InMemoryMealRepositoryImpl();

        Meal m = new Meal(LocalDateTime.of(2017, Month.AUGUST, 24, 12, 44), "Твоя еда", 500);
        thisService.save(m, 3);

        thisService.getAll(3).forEach((x) -> System.out.println(x + " before delete"));

        Objects.requireNonNull(thisService.get(m.getId(), 3), "m");

        thisService.delete(m.getId(), 3);

        Meal m1 = new Meal(LocalDateTime.of(2017, Month.AUGUST, 24, 12, 44), "222", 500);
        thisService.save(m1, 3);


        thisService.getAll(3).forEach((x) -> System.out.println(x + " after delete"));
        System.out.println("==========");

        thisService.getBetweenDates(LocalDate.of(2016, Month.AUGUST, 21),
                                    LocalDate.of(2018, Month.AUGUST, 20),
                                        3)
                .forEach(meal -> System.out.println(meal + " between"));

        m1.setId(12);
        thisService.update(m1, 3);

        thisService.getAll(3).forEach((x) -> System.out.println(x + " after update"));

    }

    @Autowired
    private MealRepository repository;

    @Override
    public Meal save(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        if (repository.get(id, userId) == null) {
            throw new NotFoundException("Meal with id: " + id + " not exist");
        }
        repository.delete(id,userId);
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        if (repository.get(id, userId) == null) {
            throw new NotFoundException("Meal with id: " + id + " not exist");
        }
        return repository.get(id, userId);
    }

    @Override
    public List<Meal> getBetweenDateTimes(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return repository.getBetween(startDateTime, endDateTime, userId);
    }

    @Override
    public Meal update(Meal meal, int userId) throws NotFoundException {
        if (repository.get(meal.getId(), userId) == null) {
            throw new NotFoundException("Meal with id: " + meal.getId() + "not exist");
        }
        return repository.save(meal, userId);

    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.getAll(userId).stream().collect(Collectors.toList());
    }
}