package com.example.demo.exception;

public class TooManyRequestsException extends RuntimeException {
    public TooManyRequestsException(String message) {
        super(message);
    }

    public TooManyRequestsException() {
        super("请求频繁，请稍后再试");
    }
}
