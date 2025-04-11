package com.leng.project.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户视图（脱敏）
 *
 */
@Data
public class UserVO implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 用户角色：user/admin/ban
     */
    private String userRole;

    /**
     * 账号状态（0- 正常 1- 封号）
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createTime;

    @Serial
    private static final long serialVersionUID = 1L;
}