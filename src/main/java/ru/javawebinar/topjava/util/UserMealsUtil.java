package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;


public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)

        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> mealsTo = new ArrayList<>();
        Map<Integer, Integer> mapColories = new HashMap<>();

        for (UserMeal userMeal : meals) {
            mapColories.merge(userMeal.getDateTime().getDayOfYear(), userMeal.getCalories(), Integer::sum);
        }

        for (UserMeal userMeal : meals) {
            int coloriesPerMeal = mapColories.get(userMeal.getDateTime().getDayOfYear());
            if (userMeal.getDateTime().toLocalTime().isAfter(startTime) && userMeal.getDateTime().toLocalTime().isBefore(endTime)) {
                if (coloriesPerMeal > caloriesPerDay) {
                    mealsTo.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), false));
                } else {
                    mealsTo.add(new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), true));
                }
            }
        }
        return mealsTo;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExcess> mealsTo = new ArrayList<>();
        Map<Integer, Integer> maps = new HashMap<>();

        meals.forEach(element -> maps.merge(element.getDateTime().getDayOfYear(), element.getCalories(), Integer::sum));

        meals.stream().filter(el -> el.getDateTime().toLocalTime().isAfter(startTime) && el.getDateTime().toLocalTime().isBefore(endTime)).
                forEach(el -> {
                    if (maps.containsKey(el.getDateTime().getDayOfYear()) && (maps.get(el.getDateTime().getDayOfYear()) <= caloriesPerDay)) {
                        mealsTo.add(new UserMealWithExcess(el.getDateTime(), el.getDescription(), el.getCalories(), true));
                    } else {
                        mealsTo.add(new UserMealWithExcess(el.getDateTime(), el.getDescription(), el.getCalories(), false));
                    }
                });

        return mealsTo;
    }
}
