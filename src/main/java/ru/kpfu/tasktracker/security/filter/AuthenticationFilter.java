package ru.kpfu.tasktracker.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.security.JwtProvider;

import java.io.IOException;

@Slf4j
@WebFilter("/*")
public class AuthenticationFilter extends HttpFilter {

    @Override
    public void init() throws ServletException {
        log.info("AuthenticationFilter initialized");
    }

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
//        chain.doFilter(req, res);
        String requestUri = req.getRequestURI();
        if (isPublicResource(requestUri)) {
            chain.doFilter(req, res);
            return;
        }

        String token = extractTokenFromCookies(req);
        if (token == null) {
            res.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (JwtProvider.validate(token)) {
            req.setAttribute("user", JwtProvider.getUserFromToken(token));
            res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            chain.doFilter(req, res);
        } else {
            removeInvalidCookie(res);
            res.sendRedirect(req.getContextPath() + "/login");
        }
    }

    private void removeInvalidCookie(HttpServletResponse res) {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setSecure(true);
        cookie.setMaxAge(0);
        res.addCookie(cookie);
    }

    private String extractTokenFromCookies(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("jwt"))
                    return cookie.getValue();
            }
        }
        return null;
    }

    private boolean isPublicResource(String requestUri) {
        return  requestUri.endsWith("/login") ||
                requestUri.endsWith("/register") ||
                requestUri.endsWith(".jsp") ||
                requestUri.endsWith(".html") ||
                requestUri.endsWith(".css") ||
                requestUri.endsWith(".js");
    }
}
