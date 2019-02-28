package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.repository.inmemory.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;
import static ru.javawebinar.topjava.web.SecurityUtil.setAuthUserId;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRepository repository;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        repository = new InMemoryMealRepositoryImpl();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        meal.setUserId(authUserId());

        log.info(meal.isNew() ? "Create {}" : "Update {}", meal);
        repository.save(meal, authUserId());
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        log.info(action);
        setAuthUserId(request.getParameter("userId") == null ? authUserId() : Integer.parseInt(request.getParameter("userId")));

        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete {}", id);
                repository.delete(id, authUserId());
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000) :
                        repository.get(getId(request), authUserId());
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAll");
                List<LocalTime> timeRange = getListOfTimeRange(request.getParameter("startTime"), request.getParameter("endTime"));
                List<LocalDate> dateRange = getListOfDateRange(request.getParameter("startDate"), request.getParameter("endDate"));
                if (timeRange != null && timeRange.size() > 0) {
                    request.setAttribute("meals",
                            MealsUtil.getFilteredWithExcess(repository.getAll(authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY,
                                    timeRange.get(0), timeRange.get(1)));
                } else if (dateRange != null && dateRange.size() > 0) {
                    request.setAttribute("meals",
                            MealsUtil.getFilteredWithExcess(repository.getAll(authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY,
                                    dateRange.get(0), dateRange.get(1)));
                } else {
                    request.setAttribute("meals",
                            MealsUtil.getWithExcess(repository.getAll(authUserId()), MealsUtil.DEFAULT_CALORIES_PER_DAY));
                }
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private List<LocalTime> getListOfTimeRange(String start, String end) {
        LocalTime startTime = start != null ? getTime(start) : null;
        LocalTime endTime = end != null ? getTime(end) : null;
        return getListOfRange(startTime, endTime);
    }

    private List<LocalDate> getListOfDateRange(String start, String end) {
        LocalDate startDate = start != null ? getDate(start) : null;
        LocalDate endDate = end != null ? getDate(end) : null;
        return getListOfRange(startDate, endDate);
    }

    private <T> List<T> getListOfRange(T start, T end) {
        if (start != null || end != null) {
            List<T> range = new ArrayList<>();
            range.add(start);
            range.add(end);
            return range;
        } else return null;
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private LocalTime getTime(String timeString) {
        return timeString.isEmpty() ? null : LocalTime.parse(timeString);
    }

    private LocalDate getDate(String dateString) {
        return dateString.isEmpty() ? null : LocalDate.parse(dateString);
    }

}
