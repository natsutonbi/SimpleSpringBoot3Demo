package com.example.demo.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.demo.exception.TooManyRequestsException;
import com.example.demo.security.utils.Const;
import com.example.demo.security.utils.FlowUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class MailService {

    @Autowired
    JavaMailSender mailSender;

    @Autowired
    ResourceLoader resourceLoader;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    FlowUtils flow;

    @Value("${spring.mail.verify-cd}")
    int verifyCdSec;

    @Value("${spring.mail.verify-available}")
    int verifyAvailableMin;

    @Value("${spring.mail.username}")
    private String from;

    // 发送验证码同时存入redis
    public void generateEmailVerifyCode(String email, String address) throws TooManyRequestsException, MailException {
        synchronized (address.intern()) {
            if (!this.ifVerifyLimited(address))
                throw new TooManyRequestsException();
            Random random = new Random();
            int code = random.nextInt(899999) + 100000;

            try {
                sendTextMail(email, "邮箱验证码", "您的邮箱验证码是：" + code + "\n请在" + verifyAvailableMin + "分钟内完成验证");
            } catch (MailException e) {
                log.error("发送邮件验证码出错", e);
                throw e;
            }

            stringRedisTemplate.opsForValue()
                    .set(Const.VERIFY_EMAIL_DATA + email, String.valueOf(code), verifyAvailableMin, TimeUnit.MINUTES);
        }
    }

    public boolean checkEmailVerifyCode(String email, String code) {
        return code.equals(getEmailVerifyCode(email));
    }

    /**
     * 删除验证码，防止重用
     * 
     * @param email 对应邮箱
     */
    public void delEmailVerifyCode(String email) {
        String key = Const.VERIFY_EMAIL_DATA + email;
        stringRedisTemplate.delete(key);
    }

    public void sendTextMail(String to, String subject, String text) throws MailException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        try {
            mailSender.send(message);
            log.info("简单邮件已经发送。");
        } catch (MailException e) {
            log.error("发送简单邮件时发生异常！", e);
            throw e;
        }
    }

    public void removeLimit(String address) {
        String key = Const.VERIFY_EMAIL_LIMIT + address;
        stringRedisTemplate.delete(key);
    }

    private boolean ifVerifyLimited(String address) {
        String key = Const.VERIFY_EMAIL_LIMIT + address;
        return flow.limitOnceCheck(key, verifyCdSec);
    }

    /**
     * 获取Redis中存储的邮件验证码
     * 
     * @param email 电邮
     * @return 验证码
     */
    private String getEmailVerifyCode(String email) {
        String key = Const.VERIFY_EMAIL_DATA + email;
        String code = stringRedisTemplate.opsForValue().get(key);
        return code;
    }
}
