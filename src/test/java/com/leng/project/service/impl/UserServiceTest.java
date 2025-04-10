package com.leng.project.service.impl;

import com.leng.lengapicommon.model.entity.User;
import com.leng.project.exception.BusinessException;
import com.leng.project.model.vo.LoginUserVO;
import com.leng.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    // 定义测试用的变量
    private static final String userName = "曾茂"; // 有效的用户名
    private static final String userAccount = "zengmao"; // 有效的用户账户
    private static final String userPassword = "123456789"; // 有效的用户密码
    private static final String wrongUserPassword = "987654321"; // 错误的用户密码
    private static final String inuserAccount = "nonexistentAccount"; // 无效的用户账户
    private static final String emptyString = ""; // 空字符串，用于测试参数为空的情况
    private static final String shortUserPassword = "123456"; // 过短的密码，用于测试密码长度不足的情况
    private static final String shortUserAccount = "mao"; // 过短的账户，用于测试账户长度不足的情况
    private static final String longUserName = "曾茂123456"; // 过长的用户名，用于测试用户名长度过长的情况
    private static final String validToken = "validToken"; // 有效的令牌，用于测试登录状态
    private static final String emptyToken = null; // 空令牌，用于测试未登录的情况

    @Test
    public void testUserRegister() {
        // 测试注册成功
        long userId = userService.userRegister(userName, userAccount, userPassword, userPassword);
        // 验证返回的用户ID是否大于0，表示注册成功
        assertTrue(userId > 0);

        // 测试参数为空的情况
        // 预期抛出BusinessException异常，表示参数为空时注册失败
        assertThrows(BusinessException.class, () -> userService.userRegister(emptyString, userAccount, userPassword, userPassword));

        // 测试用户名过长的情况
        // 预期抛出BusinessException异常，表示用户名过长时注册失败
        assertThrows(BusinessException.class, () -> userService.userRegister(longUserName, userAccount, userPassword, userPassword));

        // 测试账户过短的情况
        // 预期抛出BusinessException异常，表示账户过短时注册失败
        assertThrows(BusinessException.class, () -> userService.userRegister(userName, shortUserAccount, userPassword, userPassword));

        // 测试密码过短的情况
        // 预期抛出BusinessException异常，表示密码过短时注册失败
        assertThrows(BusinessException.class, () -> userService.userRegister(userName, userAccount, shortUserPassword, shortUserPassword));

        // 测试两次输入的密码不一致的情况
        // 预期抛出BusinessException异常，表示两次输入的密码不一致时注册失败
        assertThrows(BusinessException.class, () -> userService.userRegister(userName, userAccount, userPassword, shortUserPassword));

        // 测试账户重复的情况
        // 预期抛出BusinessException异常，表示账户重复时注册失败
        assertThrows(BusinessException.class, () -> userService.userRegister(userName, userAccount, userPassword, userPassword));
    }

    @Test
    public void testUserLogin() {
        // 测试登录成功
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword);
        // 验证返回的LoginUserVO对象是否非空，表示登录成功
        assertNotNull(loginUserVO);
        // 验证返回的令牌是否非空，表示登录成功后生成了令牌
        assertNotNull(loginUserVO.getToken());

        // 测试参数为空的情况
        // 预期抛出BusinessException异常，表示参数为空时登录失败
        assertThrows(BusinessException.class, () -> userService.userLogin(emptyString, userPassword));

        // 测试账户不存在的情况
        // 预期抛出BusinessException异常，表示账户不存在时登录失败
        assertThrows(BusinessException.class, () -> userService.userLogin(inuserAccount, userPassword));

        // 测试密码错误的情况
        // 预期抛出BusinessException异常，表示密码错误时登录失败
        assertThrows(BusinessException.class, () -> userService.userLogin(userAccount, wrongUserPassword));
    }

    @Test
    public void testGetLoginUser() {
        // 测试获取当前登录用户
        HttpServletRequest request = mock(HttpServletRequest.class); // 创建一个模拟的HTTP请求对象
        // 设置请求头中的Authorization字段为有效的令牌
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        User user = userService.getLoginUser(request); // 调用getLoginUser方法获取当前登录用户
        // 验证返回的用户对象是否非空，表示获取登录用户成功
        assertNotNull(user);

        // 测试未登录情况下获取用户信息
        // 设置请求头中的Authorization字段为空
        when(request.getHeader("Authorization")).thenReturn(emptyToken);
        // 预期抛出BusinessException异常，表示未登录时获取用户信息失败
        assertThrows(BusinessException.class, () -> userService.getLoginUser(request));
    }

    @Test
    public void testUserLogout() {
        // 测试用户注销
        HttpServletRequest request = mock(HttpServletRequest.class); // 创建一个模拟的HTTP请求对象
        // 设置请求头中的Authorization字段为有效的令牌
        when(request.getHeader("Authorization")).thenReturn("Bearer " + validToken);
        boolean result = userService.userLogout(request); // 调用userLogout方法注销用户
        // 验证返回结果是否为true，表示注销成功
        assertTrue(result);
    }
}