package com.example.demo.security.entity.vo.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class AuthorizeInfoVO {
    String username;
    List<String> roles;
    List<String> permissions;
    String token;
    Date expire;
}
