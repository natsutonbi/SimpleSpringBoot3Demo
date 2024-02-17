package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContext;

@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String,SecurityContext> securityContextRedisTemplate(@NonNull RedisConnectionFactory factory)
    {
        RedisTemplate<String,SecurityContext> template=new RedisTemplate<>();
        template.setConnectionFactory(factory);
        
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        
        template.afterPropertiesSet();
        return template;
    }
}
