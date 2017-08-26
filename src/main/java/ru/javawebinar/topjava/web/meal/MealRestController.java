package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MealRestController {

    @Autowired
    private MealService service;

    public Meal get(int id) {
        int userId = AuthorizedUser.id();
        if (null == service.get(id, userId)) {
            throw new NotFoundException("meal with id " + id + " not found");
        }
        return service.get(id, userId);
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
                service.getBetweenDates(startDate == null ? LocalDate.MIN : startDate,
                                        endDate == null ? LocalDate.MAX: endDate, userId)
                        .stream()
                        .filter(meal -> meal.getDateTime().toLocalTime().isAfter(startTime == null ? LocalTime.MIN : startTime)
                                     && meal.getDateTime().toLocalTime().isBefore(endTime == null ? LocalTime.MAX : endTime))
                        .collect(Collectors.toList()),
                                 AuthorizedUser.getCaloriesPerDay()
        );
    }

    public void delete(int id) {
        int userId = AuthorizedUser.id();
        if (null == service.get(id, userId)) {
            throw new NotFoundException("meal with id " + id + " not found");
        }
        service.delete(id, userId);
    }

    public void update(Meal meal, int id) {
        int userId = AuthorizedUser.id();
        if (null == service.get(id, userId)) {
            throw new NotFoundException("meal with id " + id + " not found");
        }
        service.update(meal, userId);
    }

}