package ru.kpfu.tasktracker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import ru.kpfu.tasktracker.dto.user.UserProfileDto;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
public class JwtProvider {

    private static final SecretKey KEY = Keys.hmacShaKeyFor(System.getenv("SECRET_KEY").getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION_TIME = 1800000; // 30 min

    public static String generateToken(UserProfileDto user) {
        return Jwts.builder()
                .claim("id", user.id())
                .claim("username", user.username())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY)
                .compact();
    }

    public static boolean validate(String token) {
        try {
            Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    public static UserProfileDto getUserFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            Long id = claims.get("id", Long.class);
            String username = claims.get("username", String.class);
            return new UserProfileDto(id, username);
        } catch (SignatureException e) {
            log.warn(e.getMessage());
            throw new IllegalArgumentException();
        }
    }

}
