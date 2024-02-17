package com.example.demo.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.example.demo.exception.EmailCodeVerifyException;
import com.example.demo.security.filter.token.EmailCodeAuthenticationToken;
import com.example.demo.security.service.MailService;
import com.example.demo.security.service.MyUserManager;

@Component
public class EmailCodeAuthenticationProvider implements AuthenticationProvider{

    @Autowired
    MailService mailService;

    @Autowired
    MyUserManager manager;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String mail = (String)authentication.getPrincipal();
        String code = (String)authentication.getCredentials();
        if(!mailService.checkEmailVerifyCode(mail, code)){
            throw new EmailCodeVerifyException("mail code wrong");
        }
        UserDetails details = manager.loadUserByEmail(mail);
        return new UsernamePasswordAuthenticationToken(details.getUsername(), null, details.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(EmailCodeAuthenticationToken.class);
    }
    
}
