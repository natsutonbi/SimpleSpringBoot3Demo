package com.example.demo.security.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.security.utils.JwtUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * 通过jwt验证
 */
@Component
public class JwtAuthorizeFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        String rawJwt = request.getHeader("Authorization");
        DecodedJWT decodedJWT = jwtUtils.resolveJwt(rawJwt);
        if (decodedJWT != null) {
            UserDetails userDetails = jwtUtils.toUser(decodedJWT);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authenticationToken);
            SecurityContextHolder.setContext(context);
            //不使用下列写法，避免线程竞争
            // SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            // request.setAttribute("username", userDetails.getUsername());
        }
        filterChain.doFilter(request, response);
    }// 每次请求

}
