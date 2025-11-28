package ru.kpfu.tasktracker.security;

import jakarta.servlet.http.Cookie;
import ru.kpfu.tasktracker.dto.user.UserProfileDto;

public class CookieManager {

    public static Cookie getCookieWithJwt(UserProfileDto user) {
        String jwt = JwtProvider.generateToken(user);
        Cookie cookie = new Cookie("jwt", jwt);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(30 * 60);
        return cookie;
    }

}
