package com.example.demo.security.filter.token;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class EmailCodeAuthenticationToken implements Authentication{

    private final String principal;
    private final String credentials;
    private final Collection<? extends GrantedAuthority> authorities;
    private boolean authenticated;


    public EmailCodeAuthenticationToken(String username, String code, Collection<? extends GrantedAuthority> authorities) {
        this.principal = username;
        this.credentials = code;
        this.authorities = authorities;
        this.authenticated = false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return principal;
    }
    
}
