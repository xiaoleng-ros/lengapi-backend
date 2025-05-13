package com.leng.project.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * 邮箱配置类
 * 用于配置邮件发送服务的相关参数
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "spring.mail")
public class EmailConfig {

    /**
     * 邮箱服务器地址
     */
    @Value("${spring.mail.host}")
    private String host;

    /**
     * 邮箱用户名
     */
    @Value("${spring.mail.username}")
    private String username;

    /**
     * 邮箱密码（授权码）
     */
    @Value("${spring.mail.password}")
    private String password;

    /**
     * 发件人邮箱
     * 获取发件人邮箱
     */
    @Value("${spring.mail.email-from}")
    private String emailFrom;

    /**
     * 配置JavaMailSender
     * 用于发送邮件的核心组件
     * @return JavaMailSender实例
     */
    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        mailSender.setDefaultEncoding("UTF-8");
        
        // 配置邮件属性
        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.smtp.ssl.enable", true);
        properties.put("mail.smtp.auth", true);
        properties.put("mail.smtp.starttls.enable", true);
        properties.put("mail.smtp.timeout", 5000);
        
        return mailSender;
    }

}