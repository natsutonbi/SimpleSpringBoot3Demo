package com.example.demo.security.filter;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import com.example.demo.security.entity.vo.request.EmailLoginVO;
import com.example.demo.security.filter.token.EmailCodeAuthenticationToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//与UsernamePasswordAuthenticationFilter类似
@Component
public class EmailCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    EmailCodeAuthenticationFilter(String url, String method, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(url, method));
        // setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(url, method));
        setAuthenticationManager(authenticationManager);
    }

    @Autowired
    EmailCodeAuthenticationFilter(@Value("${spring.mail.login-url:/api/auth/login/email}") String url, AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(url, "POST"));
        setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException, ServletException {
        // 需要是 POST 请求
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }
        // 判断请求格式是否 JSON
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            EmailLoginVO emailLogin = new ObjectMapper().readValue(request.getInputStream(), EmailLoginVO.class);
            // 获得请求参数
            String email = emailLogin.getEmail(), code = emailLogin.getCode();
            /**
             * 使用请求参数传递的邮箱和验证码，封装为一个未认证 EmailVerificationCodeAuthenticationToken 身份认证对象，
             * 然后将该对象交给 AuthenticationManager 进行认证
             */
            EmailCodeAuthenticationToken token = new EmailCodeAuthenticationToken(email,code,null);
            return this.getAuthenticationManager().authenticate(token);
        }
        return null;
    }
    
}
