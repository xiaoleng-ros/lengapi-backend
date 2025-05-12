package com.leng.project.utils;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class JwtUtils {

    private static final String SECRET_KEY = "^1AXxO^7W>0S^C4v)CnZaOpO<<e,%E-1"; // 建议使用配置文件管理密钥
    private static final long EXPIRATION_TIME = 86400000; // 24小时
    private static final long REFRESH_TIME = 43200000; // 12小时，token刷新时间

    // 使用Guava Cache实现本地令牌黑名单
    private static final Cache<String, String> tokenBlacklist = CacheBuilder.newBuilder()
            .expireAfterWrite(24, TimeUnit.HOURS)
            .maximumSize(10000) // 限制缓存大小
            .build();

    /**
     * 生成JWT令牌
     *
     * @param username 用户名
     * @return JWT令牌
     */
    public static String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("jti", UUID.randomUUID().toString()); // 添加唯一标识
        claims.put("created", new Date());

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 验证JWT令牌
     *
     * @param token JWT令牌
     * @return 用户名，如果验证失败返回null
     */
    public static String validateToken(String token) {
        if (isTokenBlacklisted(token)) {
            return null;
        }

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            // 检查是否需要刷新令牌
            Date created = new Date((Long) claims.get("created"));
            if (isTokenNeedRefresh(created)) {
                String username = claims.get("username", String.class);
                invalidateToken(token); // 将旧令牌加入黑名单
                return generateToken(username); // 生成新令牌
            }

            return claims.get("username", String.class);
        } catch (JwtException e) {
            return null;
        }
    }

    /**
     * 使令牌失效
     * 可以使用Redis实现令牌黑名单
     *
     * @param token JWT令牌
     */
    public static void invalidateToken(String token) {
        if (StringUtils.isBlank(token)) {
            return;
        }
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            String jti = claims.get("jti", String.class);
            if (jti != null) {
                tokenBlacklist.put(jti, token);
            }
        } catch (JwtException ignored) {
            // 如果令牌已经无效，则忽略异常
        }
    }

    /**
     * 检查令牌是否在黑名单中
     *
     * @param token JWT令牌
     * @return 是否在黑名单中
     */
    private static boolean isTokenBlacklisted(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            String jti = claims.get("jti", String.class);
            return jti != null && tokenBlacklist.getIfPresent(jti) != null;
        } catch (JwtException e) {
            return true;
        }
    }

    /**
     * 检查令牌是否需要刷新
     *
     * @param created 令牌创建时间
     * @return 是否需要刷新
     */
    private static boolean isTokenNeedRefresh(Date created) {
        return System.currentTimeMillis() - created.getTime() > REFRESH_TIME;
    }
}