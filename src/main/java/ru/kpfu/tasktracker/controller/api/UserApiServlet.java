package ru.kpfu.tasktracker.controller.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.controller.validator.PathValidator;
import ru.kpfu.tasktracker.controller.validator.UserValidator;
import ru.kpfu.tasktracker.dto.user.UpdatePasswordRequest;
import ru.kpfu.tasktracker.dto.user.UpdateUsernameRequest;
import ru.kpfu.tasktracker.security.JwtProvider;
import ru.kpfu.tasktracker.service.UserService;

import java.io.IOException;

@Slf4j
@WebServlet("/api/v1/users/*")
public class UserApiServlet extends HttpServlet {

    private UserService userService;
    private UserValidator userValidator;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        this.userService = (UserService) getServletContext().getAttribute("userService");
        this.userValidator = (UserValidator) getServletContext().getAttribute("userValidator");
        this.mapper = (ObjectMapper) getServletContext().getAttribute("objectMapper");
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("PATCH".equals(req.getMethod())) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null || path.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String[] pathParts = path.split("/");
        if (pathParts.length != 3) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Long userId = Long.valueOf(pathParts[1]);
        String resource = pathParts[2];
        switch (resource) {
            case "username":
                var updateUsernameRequest = mapper.readValue(req.getInputStream(), UpdateUsernameRequest.class);
                userValidator.validateUpdateUsernameRequest(updateUsernameRequest);
                String username = userService.changeUsername(userId, updateUsernameRequest.newUsername());
                updateJwt(username, resp);
                break;
            case "password":
                var updatePasswordRequest = mapper.readValue(req.getInputStream(), UpdatePasswordRequest.class);
                userValidator.validateUpdatePasswordRequest(updatePasswordRequest);
                userService.changePassword(userId, updatePasswordRequest.newPassword());
                break;
            default:
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void updateJwt(String username, HttpServletResponse resp) {
        String token = JwtProvider.generateToken(username);
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(10 * 60);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        Long userId = PathValidator.getIdFromPath(path);
        userService.softDelete(userId);
        resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

}
