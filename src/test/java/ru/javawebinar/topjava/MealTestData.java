package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;

    public static final Meal meal2 = new Meal(START_SEQ + 2, LocalDateTime.of(2019, 3, 1, 10, 0), "breakfast", 500);
    public static final Meal meal3 = new Meal(START_SEQ + 3, LocalDateTime.of(2019, 3, 1, 15, 0), "dinner", 1000);
    public static final Meal meal4 = new Meal(START_SEQ + 4, LocalDateTime.of(2019, 3, 1, 20, 0), "lunch", 1500);
    public static final Meal meal5 = new Meal(START_SEQ + 5, LocalDateTime.of(2019, 3, 2, 10, 0), "breakfast", 100);
    public static final Meal meal6 = new Meal(START_SEQ + 6, LocalDateTime.of(2019, 3, 2, 15, 0), "dinner", 200);
    public static final Meal meal7 = new Meal(START_SEQ + 7, LocalDateTime.of(2019, 3, 2, 20, 0), "lunch", 300);
}
