package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.Meals;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@WebServlet(name = "ChangeMealServlet")
public class ChangeMealServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getParameter("id") != null) {
            Meal meal = Meals.getMealsMap().get(Integer.parseInt(request.getParameter("id")));
            request.setAttribute("meal", meal);
        }
        getServletContext().getRequestDispatcher("/form.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Meal mealFromForm = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (request.getParameter("id").equals("")) {
            Meals.add(mealFromForm);
        } else {
            Meals.getMealsMap().remove(Integer.parseInt(request.getParameter("id")));
            Meals.add(mealFromForm);
        }
        response.sendRedirect("meals");
    }
}
