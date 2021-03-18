package ru.javawebinar.topjava.web;

import antlr.debug.MessageAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;


import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController extends AbstractMealController {

    public JspMealController(MealService service) {
        super(service);
    }

    @GetMapping("/meals")
    public String showAllMeals(Model model) {
        model.addAttribute("meals", super.getAll());
        return "meals";
    }

    @GetMapping("/create")
    public String createMeal(Model model) {
        Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
String name="create";
        model.addAttribute("namePage",name);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/update")
    public String updateMeal(@RequestParam("mealId") int id, Model model) {
        Meal meal = super.get(id);
        model.addAttribute("meal", meal);
        return "mealForm";
    }


    @PostMapping("/saveMeal")
    public String saveMeal(HttpServletRequest request) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.hasLength(request.getParameter("id"))) {
            int id = Integer.parseInt(request.getParameter("id"));
            super.update(meal, id);
        } else {
            super.create(meal);
        }
        return "redirect:meals";
    }

    @GetMapping("/delete")
    public String deleteMeal(@RequestParam("mealId") int id) {
        super.delete(id);
        return "redirect:meals";
    }

    public List<MealTo> getBetween(@Nullable LocalDate startDate, @Nullable LocalTime startTime,
                                   @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }

//    @GetMapping("/filter")
//    public String filterMeals(HttpServletRequest request) {
//        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
//        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
//        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
//        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
//        request.setAttribute("meals", super.getBetween(startDate, startTime, endDate, endTime));
//        return "meals";
//    }
}

