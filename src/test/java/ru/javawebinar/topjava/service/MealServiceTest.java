package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;

import static ru.javawebinar.topjava.MealTestData.*;
import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal meal = service.get(100002, USER_ID);
        assertThat(meal).isEqualTo(meal2);
    }

    @Test(expected = NotFoundException.class)
    public void getAnotherUserMeal() {
        Meal meal = service.get(100002, ADMIN_ID);
        assertThat(meal).isEqualTo(meal3);
    }

    @Test
    public void delete() {
        service.delete(100002, USER_ID);
        assertThat(service.getAll(USER_ID).size()).isEqualTo(2);
    }

    @Test(expected = NotFoundException.class)
    public void deleteAnotherUserMeal() {
        service.delete(100002, ADMIN_ID);
    }

    @Test
    public void getBetweenDateTimes() {
        assertThat(service.getBetweenDateTimes(
                LocalDateTime.of(2019, 3, 1, 0, 0),
                LocalDateTime.of(2019, 3, 3, 23, 0), ADMIN_ID).size())
                .isEqualTo(3);
        assertThat(service.getBetweenDateTimes(
                LocalDateTime.of(2019, 3, 4, 0, 0),
                LocalDateTime.of(2019, 3, 6, 23, 0), ADMIN_ID).size())
                .isEqualTo(0);
    }

    @Test
    public void getAll() {
        assertThat(service.getAll(USER_ID).size()).isEqualTo(3);
    }

    @Test
    public void update() {
        Meal updMeal = new Meal(meal2);
        updMeal.setCalories(3000);
        updMeal.setDescription("supper");
        service.update(updMeal, USER_ID);
        assertThat(updMeal).isEqualTo(service.get(meal2.getId(), USER_ID));
    }

    @Test(expected = NotFoundException.class)
    public void updateAnotherUserMeal() {
        Meal updMeal = new Meal(meal2);
        updMeal.setCalories(3000);
        updMeal.setDescription("supper");
        service.update(updMeal, ADMIN_ID);
    }

    @Test
    public void create() {
        Meal newMeal = new Meal(LocalDateTime.now(), "dinner", 1200);
        service.create(newMeal, USER_ID);
        assertThat(service.getAll(USER_ID).size()).isEqualTo(4);
        assertThat(newMeal).isEqualTo(service.get(newMeal.getId(), USER_ID));
    }
}