package ru.javawebinar.topjava.service;

import org.checkerframework.checker.units.qual.A;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.web.meal.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    MealService mealService;

    @Test
    public void get() {
        Meal mealGetTest = mealService.get(MEAL_ID, USER_ID);
        assertMatchMeal(mealGetTest, meal1);
    }

    @Test
    public void delete() {
        mealService.delete(meal1.getId(), USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL_ID, USER_ID));
    }

    @Test
    public void deleteSomeoneMeal() {
        mealService.delete(meal2.getId(), USER_ID);
        assertThrows(NotFoundException.class, () -> mealService.get(MEAL_ID, USER_ID));
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> mealsBetweenInclusive = mealService.getBetweenInclusive(LocalDate.of(2015, Month.JUNE, 1),
                LocalDate.of(2015, Month.JUNE, 1), USER_ID);
    }

    @Test
    public void getAll() {
        List<Meal> all = mealService.getAll(USER_ID);
        assertMatchMeal(all, meal1);
    }

    @Test
    public void getAllSomeoneMeal() {
        List<Meal> all = mealService.getAll(USER_ID);
        assertMatchMeal(all, meal1, meal2);
    }

    @Test
    public void update() {
        Meal created = mealService.create(meal1, USER_ID);
        Integer newId = created.getId();
        Meal oldMeal = meal1;
        oldMeal.setId(newId);
        assertMatchMeal(meal1, oldMeal);
        assertMatchMeal(mealService.get(newId, USER_ID), oldMeal);
    }

    @Test
    public void updatingSomeoneMeal() {
        Meal created = mealService.create(meal1, ADMIN_ID);
        Integer newId = created.getId();
        Meal oldMeal = meal1;
        oldMeal.setId(newId);
        assertMatchMeal(meal1, oldMeal);
        assertMatchMeal(mealService.get(newId, ADMIN_ID), oldMeal);
    }

    @Test
    public void create() {
        Meal created = mealService.create(getNewMeal(), USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNewMeal();
        newMeal.setId(newId);
        assertMatchMeal(created, newMeal);
        assertMatchMeal(mealService.get(newId, USER_ID), newMeal);
    }

    @Test
    public void duplicateMealCreate() {
        assertThrows(DataAccessException.class, () ->
                mealService.create(new Meal(null, LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Юзер ланч,дубликат", 510), USER_ID));
    }
}