package ru.kpfu.tasktracker.controller.page;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.dto.user.UserCreateDto;
import ru.kpfu.tasktracker.dto.user.UserProfileDto;
import ru.kpfu.tasktracker.security.CookieManager;
import ru.kpfu.tasktracker.security.JwtProvider;
import ru.kpfu.tasktracker.service.UserService;

import java.io.IOException;

@Slf4j
@WebServlet("/login")
public class LoginPageServlet extends HttpServlet {

    private UserService userService;

    @Override
    public void init() throws ServletException {
        this.userService = (UserService) getServletContext().getAttribute("userService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserCreateDto dto = new UserCreateDto(req.getParameter("username"), req.getParameter("password"));
        if (!userService.authenticate(dto)) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        UserProfileDto user = userService.findByUsername(dto.username());
        Cookie cookie = CookieManager.getCookieWithJwt(user);
        resp.addCookie(cookie);
        resp.sendRedirect(req.getContextPath() + "/home");
    }
}
