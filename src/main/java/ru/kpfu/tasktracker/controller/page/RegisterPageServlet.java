package ru.kpfu.tasktracker.controller.page;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.controller.validator.UserValidator;
import ru.kpfu.tasktracker.dto.user.UserCreateDto;
import ru.kpfu.tasktracker.dto.user.UserProfileDto;
import ru.kpfu.tasktracker.security.CookieManager;
import ru.kpfu.tasktracker.security.JwtProvider;
import ru.kpfu.tasktracker.service.UserService;

import java.io.IOException;

@Slf4j
@WebServlet("/register")
public class RegisterPageServlet extends HttpServlet {

    private UserService userService;
    private UserValidator userValidator;

    @Override
    public void init() throws ServletException {
        this.userService = (UserService) getServletContext().getAttribute("userService");
        this.userValidator = (UserValidator) getServletContext().getAttribute("userValidator");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserCreateDto dto = new UserCreateDto(req.getParameter("username"), req.getParameter("password"));
        userValidator.validateNewUser(dto);
        UserProfileDto user = userService.save(dto);

        Cookie jwtCookie = CookieManager.getCookieWithJwt(user);
        resp.addCookie(jwtCookie);
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}
