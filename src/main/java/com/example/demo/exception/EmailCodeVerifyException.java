package com.example.demo.exception;

import org.springframework.security.core.AuthenticationException;

public class EmailCodeVerifyException extends AuthenticationException{

    public EmailCodeVerifyException(String msg) {
        super(msg);
    }
}
