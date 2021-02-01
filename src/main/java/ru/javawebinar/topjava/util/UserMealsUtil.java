package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 11, 59), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)

        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<Integer, Integer> mapCalories = new HashMap<>();
        for (UserMeal userMeal : meals) {
            mapCalories.merge(userMeal.getDateTime().getDayOfYear(), userMeal.getCalories(), Integer::sum);
        }

        List<UserMealWithExcess> mealsTo = new ArrayList<>();
        for (UserMeal userMeal : meals) {
            int caloriesPerMeal = mapCalories.get(userMeal.getDateTime().getDayOfYear());
            if (TimeUtil.isBetweenHalfOpen(userMeal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealsTo.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), (caloriesPerMeal <= caloriesPerDay)));
            }
        }
        return mealsTo;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<Integer, Integer> maps = new HashMap<>();
        meals.forEach(element -> maps.merge(element.getDateTime().getDayOfYear(), element.getCalories(), Integer::sum));

        List<UserMealWithExcess> mealsTo = new ArrayList<>();
        meals.stream().filter(el -> TimeUtil.isBetweenHalfOpen(el.getDateTime().toLocalTime(), startTime, endTime)).
                map(el -> new UserMealWithExcess(el.getDateTime(), el.getDescription(), el.getCalories(),
                        maps.get(el.getDateTime().getDayOfYear()) <= caloriesPerDay)).
                forEach(mealsTo::add);

        return mealsTo;
    }
}
