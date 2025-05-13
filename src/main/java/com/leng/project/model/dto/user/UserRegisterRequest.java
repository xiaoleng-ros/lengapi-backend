package com.leng.project.model.dto.user;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {

    @Serial
    private static final long serialVersionUID = 3191241716373120793L;

    private String userName;

    private String userAccount;
    
    /**
     * 邮箱
     */
    private String email;

    private String userPassword;

    private String checkPassword;
    
    /**
     * 验证码
     */
    private String verificationCode;
}
