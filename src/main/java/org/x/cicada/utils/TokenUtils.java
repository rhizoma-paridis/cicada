package org.x.cicada.utils;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class TokenUtils {

    private static final long JWT_EXPIRATION = 5 * 60 * 1000L; // 五分钟过期

    public static final String TOKEN_PREFIX = "Bearer "; // token 的开头字符串

    public static final String KEY = "test";

    public static String generateToken(String username) {
        Date now = new Date();
        Date expireDate = new Date(System.currentTimeMillis() + JWT_EXPIRATION); // 设置过期时间
        String token = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, KEY)
                .compact();
        return token;

    }

    public static Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            System.out.println("error token");
        }
        return claims;
    }
}
