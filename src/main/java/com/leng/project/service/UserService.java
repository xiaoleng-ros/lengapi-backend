package com.leng.project.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.leng.lengapicommon.model.entity.User;
import com.leng.project.model.dto.user.UserQueryRequest;
import com.leng.project.model.vo.LoginUserVO;
import com.leng.project.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 用户服务
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     */
    long userRegister(String userName, String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     */
    LoginUserVO userLogin(String userAccount, String userPassword);

    /**
     * 获取当前登录用户
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 获取当前登录用户（允许未登录）
     */
    User getLoginUserPermitNull(HttpServletRequest request);

    /**
     * 是否为管理员
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     */
    boolean isAdmin(User user);

    /**
     * 用户注销
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏的已登录用户信息
     */
    LoginUserVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息列表
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取查询条件
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 用户头像上传
     */
    void updateUserAvatar(Long id, String fileUrl);
}