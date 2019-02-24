package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meals;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteMealServlet")
public class DeleteMealServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Meals.delete(Integer.parseInt(request.getParameter("id")));
        response.sendRedirect("meals");
    }
}
