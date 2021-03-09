package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
@ActiveProfiles({"postgres","datajpa"})
public class MealServiceTest  {

    @Autowired
    private MealService service;

    @Test
    public void delete() {
        service.delete(MEAL1_ID, USER_ID);
        assertThrows(NotFoundException.class, () ->
                service.get(MEAL1_ID, USER_ID));
    }

    @Test
    public void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND, USER_ID));
    }

    @Test
    public void deleteNotOwn() {
        assertThrows(NotFoundException.class, () -> service.delete(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(), USER_ID);
        int newId = created.id();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        created.setUser(null);
        MEAL_MATCHER.assertMatch(created, newMeal);
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () ->
                service.create(new Meal(null, meal1.getDateTime(), "duplicate", 100), USER_ID));
    }

    @Test
    public void get() {
        Meal actual = service.get(ADMIN_MEAL_ID, ADMIN_ID);
        actual.setUser(null);
        MEAL_MATCHER.assertMatch(actual, adminMeal1);
    }

    @Test
    public void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND, USER_ID));
    }

    @Test
    public void getNotOwn() {
        assertThrows(NotFoundException.class, () -> service.get(MEAL1_ID, ADMIN_ID));
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated, USER_ID);
        Meal meal = service.get(MEAL1_ID, USER_ID);
        meal.setUser(null);
        MEAL_MATCHER.assertMatch(meal, getUpdated());
    }

    @Test
    public void updateNotOwn() {
        assertThrows(NotFoundException.class, () -> service.update(meal1, ADMIN_ID));
        Meal meal = service.get(MEAL1_ID, USER_ID);
        meal.setUser(null);
        MEAL_MATCHER.assertMatch(meal, meal1);
    }

    @Test
    public void getAll() {
        List<Meal> mealsGetAll = service.getAll(USER_ID);
        for (Meal meals : mealsGetAll) {
            meals.setUser(null);
        }
        MEAL_MATCHER.assertMatch(mealsGetAll, meals);
    }

    @Test
    public void getBetweenInclusive() {
        List<Meal> getBetweenInclusive = service.getBetweenInclusive(
                LocalDate.of(2020, Month.JANUARY, 30),
                LocalDate.of(2020, Month.JANUARY, 30), USER_ID);
        for (Meal meals : getBetweenInclusive) {
            meals.setUser(null);
        }
        MEAL_MATCHER.assertMatch(getBetweenInclusive,
                meal3, meal2, meal1);
    }

    @Test
    public void getBetweenWithNullDates() {
        List<Meal> getBetweenWithNullDates = service.getBetweenInclusive(null, null, USER_ID);
        for (Meal meals : getBetweenWithNullDates) {
            meals.setUser(null);
        }
        MEAL_MATCHER.assertMatch(getBetweenWithNullDates, meals);
    }
}