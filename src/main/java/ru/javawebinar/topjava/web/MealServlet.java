package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredWithExceeded;

/**
 * Created by vlad on 27-Jul-17.
 */

public class MealServlet extends HttpServlet {


    private static final Logger LOG = getLogger(MealServlet.class);



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        LOG.debug("redirect to meals");
        String action = req.getParameter("action");

        List<MealWithExceed> meals = getFilteredWithExceeded(
                 MealsUtil.MEALS,
                LocalTime.MIN,
                LocalTime.MAX,
                2000);

        if (action.equalsIgnoreCase("listMeals")) {



//            meals.forEach(m -> System.out.println("============" + m.toString()));
//            req.setAttribute("meals", meals.get(1));
        }


        req.setAttribute("listMeals", meals.get(1));


        RequestDispatcher view = req.getRequestDispatcher("meals.jsp");
        view.forward(req, resp);
//        resp.sendRedirect("meals.jsp");
    }
}