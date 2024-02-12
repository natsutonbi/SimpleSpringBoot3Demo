package com.example.demo.utils;

import java.io.IOException;

import jakarta.servlet.http.HttpServletResponse;

public class webUtils {
    public static String renderString(HttpServletResponse response,String str){
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(str);//写到响应体
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
