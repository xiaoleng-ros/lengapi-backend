package com.leng.project.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtils {

    private static final String SECRET_KEY = "your-256-bit-secret-key"; // 密钥
    private static final long EXPIRATION_TIME = 86400000; // 过期时间（毫秒），这里设置为 24 小时

    /**
     * 生成 JWT 令牌
     *
     * @param username 用户名
     * @return JWT 令牌
     */
    public static String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 验证 JWT 令牌
     *
     * @param token JWT 令牌
     * @return 用户名
     */
    public static String validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("username", String.class);
        } catch (JwtException e) {
            // 捕获 JWT 异常，例如过期、签名错误等
            return null;
        }
    }
}