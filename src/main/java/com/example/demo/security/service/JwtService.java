package com.example.demo.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.security.config.securityConfig;
import com.example.demo.security.utils.JwtUtils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

@Service
public class JwtService {
    @Autowired
    JwtUtils jwtUtils;
    

    public String creatJwt(Authentication authentication){
        Date expireDate = jwtUtils.getExpireTime();
        return this.creatJwt(expireDate,authentication);
    }

    public String creatJwt(Date expireDate, Authentication authentication){
        UserDetails userDetails = (UserDetails)authentication.getPrincipal();
        String token = jwtUtils.createJwt(userDetails,expireDate,securityConfig.rolePrefix);
        return token;
    }

    public boolean invalidateJwt(HttpServletRequest request){
        String rawJwt = request.getHeader("Authorization");
        if(rawJwt == null) return false;
        return jwtUtils.invalidateJwt(rawJwt);
    }
}
