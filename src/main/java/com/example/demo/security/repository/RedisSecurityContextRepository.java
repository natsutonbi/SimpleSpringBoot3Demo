package com.example.demo.security.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.DeferredSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class RedisSecurityContextRepository implements SecurityContextRepository{

    private final RedisTemplate<String, SecurityContext> redisTemplate;

    RedisSecurityContextRepository(RedisTemplate<String, SecurityContext> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        String sessionId = request.getRequestedSessionId();
        return redisTemplate.hasKey(sessionId);
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        String sessionId = request.getRequestedSessionId();
        return redisTemplate.opsForValue().get(sessionId);
    }

    @Override
    public DeferredSecurityContext loadDeferredContext(HttpServletRequest request){
        String sessionId = request.getRequestedSessionId();
        return new DeferredRedisSecurityContext(this.redisTemplate, sessionId);
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        String sessionId = request.getSession().getId();
        redisTemplate.opsForValue().set(sessionId, context);
    }

}


/**
 * DeferredRedisSecurityContext
 */
class DeferredRedisSecurityContext implements DeferredSecurityContext {

    SecurityContext value;
    boolean missingValue;

    RedisTemplate<String, SecurityContext> redisTemplate;
    String sessionId;

    DeferredRedisSecurityContext(RedisTemplate<String, SecurityContext> redisTemplate, String sessionId){
        this.redisTemplate = redisTemplate;
        this.sessionId = sessionId;
    }

    @Override
    public SecurityContext get() {
        init();
        return this.value;
    }

    @Override
    public boolean isGenerated() {
        init();
        return this.missingValue;
    }

    void init(){
        if(this.value != null){
            return;
        }
        this.missingValue = redisTemplate.hasKey(sessionId);
        if(this.missingValue)
            this.value = SecurityContextHolder.createEmptyContext();
        else
            this.value = redisTemplate.opsForValue().get(sessionId);
    }
    
}