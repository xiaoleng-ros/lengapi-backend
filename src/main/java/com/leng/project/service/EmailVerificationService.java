package com.leng.project.service;

import java.util.concurrent.CompletableFuture;

/**
 * 邮箱验证码服务
 */
public interface EmailVerificationService {
    
    /**
     * 发送验证码
     * @param email 邮箱地址
     * @return Future<Boolean> 异步执行结果
     */
    CompletableFuture<Boolean> sendVerificationCode(String email);
    
    /**
     * 验证验证码是否正确
     * @param email 邮箱地址
     * @param code 验证码
     * @return 是否验证成功
     */
    boolean verifyCode(String email, String code);
}