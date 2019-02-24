package ru.javawebinar.topjava.model;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Meals {
    private static int counter = 0;
    private static ConcurrentMap<Integer, Meal> mealsMap;

    static  {
        mealsMap = new ConcurrentHashMap<>();
        Meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500));
        Meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000));
        Meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500));
        Meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000));
        Meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500));
        Meals.add(new Meal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510));
    }

    public synchronized static void add(Meal meal) {
        meal.setId(++counter);
        mealsMap.put(meal.getId(), meal);
    }

    public static ConcurrentMap<Integer, Meal> getMealsMap() {
        return mealsMap;
    }

    public synchronized static void delete(int id) {
        mealsMap.remove(id);
    }

}
