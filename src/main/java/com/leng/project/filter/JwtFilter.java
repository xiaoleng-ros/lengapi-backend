package com.leng.project.filter;

import com.leng.project.utils.JwtUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JwtFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain)
            throws ServletException, IOException {

        // 放行 Swagger 相关路径
        if (request.getRequestURI().startsWith("/swagger-ui") ||
                request.getRequestURI().startsWith("/doc.html") ||
                request.getRequestURI().startsWith("/v2/api-docs") ||
                request.getRequestURI().startsWith("/swagger-resources") ||
                request.getRequestURI().startsWith("/webjars") ||
                request.getRequestURI().startsWith("/v3/api-docs") ||
                request.getRequestURI().startsWith("/swagger-ui.html") ||
                // 放行接口展示页面数据和接口详情页面数据
                request.getRequestURI().startsWith("/interfaceInfo/list/page") ||
                request.getRequestURI().startsWith("/interfaceInfo/get/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // 去掉前缀 "Bearer "

            String username = JwtUtils.validateToken(token);

            if (username != null) {
                // 设置 Spring Security 上下文
                UsernamePasswordAuthenticationToken authentication
                        = new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}