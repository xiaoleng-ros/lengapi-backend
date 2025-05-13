package com.leng.project.constant;

/**
 * 电子邮件常量类
 * 用于定义邮件相关的常量
 */
public class EmailConstant {

    /**
     * 邮件主题前缀
     */
    public static final String SUBJECT_PREFIX = "[API接口应用平台] ";
    
    /**
     * 验证码邮件主题
     */
    public static final String VERIFICATION_CODE_SUBJECT = SUBJECT_PREFIX + "验证码";
    
    /**
     * 注册成功邮件主题
     */
    public static final String REGISTRATION_SUCCESS_SUBJECT = SUBJECT_PREFIX + "注册成功";
    
    /**
     * 密码重置邮件主题
     */
    public static final String PASSWORD_RESET_SUBJECT = SUBJECT_PREFIX + "密码重置";
    
    /**
     * 验证码有效期（分钟）
     */
    public static final int VERIFICATION_CODE_EXPIRATION = 5;
    
    /**
     * 验证码长度
     */
    public static final int VERIFICATION_CODE_LENGTH = 6;
    
    /**
     * 邮件发送成功状态码
     */
    public static final String SEND_SUCCESS = "200";
    
    /**
     * 邮件发送失败状态码
     */
    public static final String SEND_FAILURE = "500";
    
    /**
     * 邮件模板路径
     */
    public static final String TEMPLATE_PATH = "templates/email/";
    
    /**
     * 验证码邮件模板名称
     */
    public static final String VERIFICATION_CODE_TEMPLATE = "verification-code";
    
    /**
     * 注册成功邮件模板名称
     */
    public static final String REGISTRATION_SUCCESS_TEMPLATE = "registration-success";
    
    /**
     * 密码重置邮件模板名称
     */
    public static final String PASSWORD_RESET_TEMPLATE = "password-reset";
    
    /**
     * 邮件内容类型 - HTML
     */
    public static final String CONTENT_TYPE_HTML = "text/html;charset=UTF-8";
    
    /**
     * 邮件内容类型 - 纯文本
     */
    public static final String CONTENT_TYPE_TEXT = "text/plain;charset=UTF-8";
}