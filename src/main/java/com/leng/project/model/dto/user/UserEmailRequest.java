package com.leng.project.model.dto.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 邮箱请求
 */
@Data
public class UserEmailRequest implements Serializable {
    
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 邮箱地址
     */
    private String email;
}