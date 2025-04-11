package com.leng.project.constant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 文件常量
 *
 */
@Component
public class FileConstant {

    /**
     * 腾讯云COS 访问地址
     */
    @Value("${file.cos.host}")
    private String cosHost;

    public static String COS_HOST;

    @PostConstruct
    public void init() {
        COS_HOST = cosHost;
    }

    public static String getCosHost() {
        return COS_HOST;
    }
}