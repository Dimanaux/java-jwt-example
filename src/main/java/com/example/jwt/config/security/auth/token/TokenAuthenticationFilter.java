package com.example.jwt.config.security.auth.token;

import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class TokenAuthenticationFilter implements Filter {
    private final static String AUTH_HEADER = "AUTH";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String tokenValue = request.getHeader(AUTH_HEADER);
        if (tokenValue != null) {
            // создаем объект токен-аутентификации
            TokenAuthentication authentication = new TokenAuthentication();
            // в него кладем токен
            authentication.setToken(tokenValue);
            // отдаем контексту
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        // отдаем запрос дальше (его встретит либо другой фильтр, либо что-то еще)
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
