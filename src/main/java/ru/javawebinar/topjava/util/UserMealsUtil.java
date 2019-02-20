package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        getFilteredWithExceededStream(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        getFilteredWithExceededOptional(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
    }

    private static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        ArrayList<UserMealWithExceed> filteredList = new ArrayList<>();
        for (UserMeal meal : mealList) {
            if (!TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) continue;
            int sum = 0;
            for (UserMeal mealInner : mealList) {
                if (mealInner.getDateTime().toLocalDate().compareTo(meal.getDateTime().toLocalDate()) == 0)
                    sum += mealInner.getCalories();
            }
            if (sum > caloriesPerDay) {
                filteredList.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true));
            } else
                filteredList.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), false));
        }
        return filteredList;
    }

    private static List<UserMealWithExceed> getFilteredWithExceededStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        ArrayList<UserMealWithExceed> filteredList;
        filteredList = mealList
                .stream()
                .filter(meal -> TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime))
                .map(meal -> {
                    int sum = 0;
                    for (UserMeal mealInner : mealList) {
                        if (mealInner.getDateTime().toLocalDate().compareTo(meal.getDateTime().toLocalDate()) == 0)
                            sum += mealInner.getCalories();
                    }
                    if (sum > caloriesPerDay) {
                        return new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true);
                    } else
                        return new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), false);
                })
                .collect(Collectors.toCollection(ArrayList::new));
        return filteredList;
    }

    private static List<UserMealWithExceed> getFilteredWithExceededOptional(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        ArrayList<UserMealWithExceed> filteredList = new ArrayList<>();
        Map<LocalDate, Integer> dates = new HashMap<>();
        for (UserMeal meal : mealList) {
            dates.put(meal.getDateTime().toLocalDate(), dates.getOrDefault(meal.getDateTime().toLocalDate(), 0) + meal.getCalories());
        }
        for (UserMeal meal : mealList) {
            if (!TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) continue;
            filteredList.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), (dates.get(meal.getDateTime().toLocalDate()) > caloriesPerDay)));
        }
        return filteredList;
    }
}
