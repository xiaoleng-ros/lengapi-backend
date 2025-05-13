package com.leng.project.service.impl;

import com.leng.project.constant.EmailConstant;
import com.leng.project.service.EmailVerificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 邮箱验证码服务实现类
 */
@Slf4j
@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {
    
    @Resource
    private JavaMailSender mailSender;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 发件人邮箱地址
     */
    @Value("${spring.mail.email-from}")
    private String emailFrom;

    /**
     * Redis中验证码的key前缀
     */
    private static final String VERIFICATION_CODE_KEY_PREFIX = "email:verification:code:";

    /**
     * Redis中验证码发送记录的key前缀
     */
    private static final String SEND_RECORD_KEY_PREFIX = "email:verification:send:";

    /**
     * 验证码发送最小间隔时间（秒）
     */
    private static final long MIN_SEND_INTERVAL = 60;

    @Override
    public boolean sendVerificationCode(String email) {
        try {
            // 检查发送频率
            String sendRecordKey = SEND_RECORD_KEY_PREFIX + email;
            String lastSendTime = stringRedisTemplate.opsForValue().get(sendRecordKey);
            
            if (lastSendTime != null) {
                long timeDiff = System.currentTimeMillis() - Long.parseLong(lastSendTime);
                if (timeDiff < MIN_SEND_INTERVAL * 1000) {
                    log.warn("验证码发送过于频繁，邮箱: {}, 剩余等待时间: {}秒", email, 
                            (MIN_SEND_INTERVAL * 1000 - timeDiff) / 1000);
                    return false;
                }
            }

            // 生成6位随机验证码
            String verificationCode = generateVerificationCode();
            log.info("开始发送验证码到邮箱: {}", email);
            
            // 发送验证码邮件
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(emailFrom);
            message.setTo(email);
            message.setSubject(EmailConstant.VERIFICATION_CODE_SUBJECT);
            message.setText("您的验证码是：" + verificationCode + "，有效期" + 
                    EmailConstant.VERIFICATION_CODE_EXPIRATION + "分钟，请勿泄露给他人。");
            
            mailSender.send(message);
            log.info("验证码发送成功");
            
            // 将验证码存储到Redis
            String redisKey = VERIFICATION_CODE_KEY_PREFIX + email;
            stringRedisTemplate.opsForValue().set(redisKey, verificationCode, 
                    EmailConstant.VERIFICATION_CODE_EXPIRATION, TimeUnit.MINUTES);
            
            // 记录发送时间
            stringRedisTemplate.opsForValue().set(sendRecordKey, 
                    String.valueOf(System.currentTimeMillis()), 
                    EmailConstant.VERIFICATION_CODE_EXPIRATION, 
                    TimeUnit.MINUTES);
            
            return true;
        } catch (Exception e) {
            log.error("验证码发送失败，邮箱: {}, 错误信息: {}", email, e.getMessage());
            return false;
        }
    }

    /**
     * 验证验证码
     */
    @Override
    public boolean verifyCode(String email, String code) {
        // 参数校验
        if (StringUtils.isAnyBlank(email, code)) {
            return false;
        }
        
        try {
            // 构建Redis中存储的key
            String redisKey = VERIFICATION_CODE_KEY_PREFIX + email;
            
            // 从Redis中获取验证码
            String storedCode = stringRedisTemplate.opsForValue().get(redisKey);
            
            // 验证码不存在或已过期
            if (storedCode == null) {
                return false;
            }
            
            // 验证码匹配验证
            boolean isMatch = storedCode.equals(code);
            
            if (isMatch) {
                // 验证成功后，立即删除验证码，防止重复使用
                stringRedisTemplate.delete(redisKey);
            }
            
            return isMatch;
        } catch (Exception e) {
            log.error("验证码验证失败", e);
            return false;
        }
    }

    /**
     * 生成6位随机验证码
     */
    private String generateVerificationCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < EmailConstant.VERIFICATION_CODE_LENGTH; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}