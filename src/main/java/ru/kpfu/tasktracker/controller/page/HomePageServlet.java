package ru.kpfu.tasktracker.controller.page;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.kpfu.tasktracker.dto.user.UserDto;
import ru.kpfu.tasktracker.dto.user.UserProfileDto;
import ru.kpfu.tasktracker.service.UserService;

import java.io.IOException;

@WebServlet("/home")
public class HomePageServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserProfileDto userData = (UserProfileDto) req.getAttribute("user");
        UserDto user = userService.findByUsernameWithProjects(userData.username());
        req.setAttribute("user", user);
        req.getRequestDispatcher("index.jsp").forward(req, resp);
    }
}
