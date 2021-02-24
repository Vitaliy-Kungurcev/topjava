package ru.javawebinar.topjava.web.meal;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID = START_SEQ;
    public static final int MEAL2_ID = START_SEQ+1 ;
    public static final int NOT_FOUNDMEAL = 10;

    public static final  Meal meal1=new Meal(MEAL_ID,LocalDateTime.of(2015, Month.JUNE, 1, 14, 0), "Юзер ланч", 510) ;
    public static final  Meal meal2=new Meal(MEAL2_ID,LocalDateTime.of(2015, Month.JUNE, 1, 21, 0), "Админ ужин", 1500);

    public static Meal getNewMeal() {
        return new Meal(null, LocalDateTime.of(2015, Month.JUNE, 1, 16, 0), "новая еда", 600);
    }


    public static void assertMatchMeal(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().ignoringFields("registered", "roles").isEqualTo(expected);
    }

    public static void assertMatchMeal(Iterable<Meal> actual, Meal... expected) {
        assertMatchMeal(actual, Arrays.asList(expected));
    }

    public static void assertMatchMeal(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("registered", "roles").isEqualTo(expected);
    }
}
