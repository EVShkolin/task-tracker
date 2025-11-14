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
import ru.kpfu.tasktracker.dto.user.UserResponseDto;
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
        UserResponseDto user = userService.save(dto);

        String token = JwtProvider.generateToken(user.username());
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(10 * 60);
        cookie.setPath("/");
        resp.addCookie(cookie);
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}
