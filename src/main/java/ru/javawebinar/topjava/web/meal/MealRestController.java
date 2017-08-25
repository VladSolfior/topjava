package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class MealRestController {

    @Autowired
    private MealService service;

    public Meal get(int id) {
        return service.get(id, AuthorizedUser.id());
    }

    public Meal create(Meal meal) {

        return service.save(meal, AuthorizedUser.id());
    }

    public List<MealWithExceed> getAll() {
        return MealsUtil.getWithExceeded(
                service.getAll(
                        AuthorizedUser.id()),
                        AuthorizedUser.getCaloriesPerDay());
    }

    /**
     * <ol>Filter separately
     *   <li>by date</li>
     *   <li>by time for every date</li>
     * </ol>
     */
    public List<MealWithExceed> getBeetwen(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        int userId = AuthorizedUser.id();
        return MealsUtil.getWithExceeded(
                service.getBetweenDates(startDate, endDate, userId)
                        .stream()
                        .filter(meal -> meal.getDateTime().toLocalTime().isAfter(startTime)
                                     && meal.getDateTime().toLocalTime().isBefore(endTime))
                        .collect(Collectors.toList()),
                                 AuthorizedUser.getCaloriesPerDay()
        );
    }

    public void delete(int id) {
        int userId = AuthorizedUser.id();
        Objects.requireNonNull(service.get(id, userId), "meal don't exist");
        service.delete(id, userId);
    }

    public void update(Meal meal, int id) {
        int userId = AuthorizedUser.id();
        Objects.requireNonNull(service.get(id, userId), "meal don't exist");
        service.update(meal, userId);
    }

}