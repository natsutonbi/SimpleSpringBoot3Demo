package com.example.demo.security.entity.vo.request;

import lombok.Data;

@Data
public class EmailMessageVO {
    String reciever_email;
    String reciever_username;
    String content;
}
