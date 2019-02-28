package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryMealRepositoryImpl.class);

    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(meal -> save(meal, 0));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        log.warn("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            log.warn("save isNew {}", meal);
            return meal;
        } else {
            log.warn("else {}", meal.toString());
            if (meal.getUserId() != userId) return null;
            return repository.computeIfPresent(meal.getId(), (integer, meal1) -> meal);
        }
    }

    @Override
    public boolean delete(int id, int userId) {
        log.info("delete {}", id);
        if (checkUserHasNoMeals(userId)) return false;
        if (repository.containsKey(id) && repository.get(id).getUserId() == userId) {
            repository.remove(id);
            return true;
        } else return false;
    }

    @Override
    public Meal get(int id, int userId) {
        log.info("get {}", id);
        if (checkUserHasNoMeals(userId)) return null;
        if (repository.containsKey(id) && repository.get(id).getUserId() == userId) {
            return repository.get(id);
        } else return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return checkUserHasNoMeals(userId) ?
                Collections.emptyList() :
                repository
                        .values()
                        .stream()
                        .filter(meal -> meal.getUserId() == userId)
                        .sorted((o1, o2) -> o2.getDateTime().compareTo(o1.getDateTime()))
                        .collect(Collectors.toList());
    }

    private boolean checkUserHasNoMeals(int userId) {
        return repository.values().stream().noneMatch(meal -> meal.getUserId() == userId);
    }
}

