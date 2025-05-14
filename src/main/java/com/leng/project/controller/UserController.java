package com.leng.project.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leng.lengapicommon.model.entity.User;
import com.leng.project.annotation.AuthCheck;
import com.leng.project.common.BaseResponse;
import com.leng.project.common.ErrorCode;
import com.leng.project.common.ResultUtils;
import com.leng.project.constant.UserConstant;
import com.leng.project.exception.BusinessException;
import com.leng.project.exception.ThrowUtils;
import com.leng.project.model.dto.user.*;
import com.leng.project.model.vo.LoginUserVO;
import com.leng.project.model.vo.UserVO;
import com.leng.project.service.UserService;
import com.leng.project.service.EmailVerificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


/**
 * 用户接口
 *
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    @Resource
    private UserService userService;
    
    @Resource
    private EmailVerificationService emailVerificationService;

    /**
     * 发送验证码
     * @param useremailRequest 邮箱请求体
     * @return 发送结果
     */
    @PostMapping("/send/verification-code")
    public BaseResponse<Boolean> sendVerificationCode(@RequestBody UserEmailRequest useremailRequest) {
        if (useremailRequest == null || StringUtils.isBlank(useremailRequest.getEmail())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        try {
            CompletableFuture<Boolean> future = emailVerificationService.sendVerificationCode(useremailRequest.getEmail());
            // 这里我们不等待结果，直接返回true表示验证码发送请求已接收
            return ResultUtils.success(true);
        } catch (Exception e) {
            log.error("发送验证码失败", e);
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "验证码发送失败");
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userName = userRegisterRequest.getUserName();
        String userAccount = userRegisterRequest.getUserAccount();
        String email = userRegisterRequest.getEmail();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String verificationCode = userRegisterRequest.getVerificationCode();
        
        // 判断是使用账号注册还是邮箱注册
        if (StringUtils.isNotBlank(email)) {
            // 邮箱注册
            if (StringUtils.isAnyBlank(userName, email, userPassword, checkPassword, verificationCode)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
            }
            // 验证验证码
            boolean isValid = emailVerificationService.verifyCode(email, verificationCode);
            if (!isValid) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误或已过期");
            }
            long result = userService.userEmailRegister(userName, email, verificationCode, userPassword, checkPassword);
            return ResultUtils.success(result);
        } else {
            // 账号注册
            if (StringUtils.isAnyBlank(userName, userAccount, userPassword, checkPassword)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
            }
            long result = userService.userRegister(userName, userAccount, userPassword, checkPassword);
            return ResultUtils.success(result);
        }
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public BaseResponse<Map<String, Object>> userLogin(@RequestBody UserLoginRequest userLoginRequest) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        String email = userLoginRequest.getEmail();
        String verificationCode = userLoginRequest.getVerificationCode();
        
        // 判断是使用账号登录还是邮箱登录
        LoginUserVO loginUserVO;
        if (StringUtils.isNotBlank(email)) {
            // 邮箱登录
            if (StringUtils.isAnyBlank(email, verificationCode)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱和验证码不能为空");
            }
            // 验证验证码
            boolean isValid = emailVerificationService.verifyCode(email, verificationCode);
            if (!isValid) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误或已过期");
            }
            loginUserVO = userService.userEmailLogin(email, verificationCode);
        } else {
            // 账号登录
            if (StringUtils.isAnyBlank(userAccount, userPassword)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号和密码不能为空");
            }
            loginUserVO = userService.userLogin(userAccount, userPassword);
        }
        
        // 返回包含令牌的响应
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("token", loginUserVO.getToken());
        responseMap.put("user", loginUserVO);
        return ResultUtils.success(responseMap);
    }

    /**
     * 用户退出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    /**
     * 管理员创建用户
     *
     * @param userAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)  // 管理员权限检查
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
        if (userAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 默认密码 123456789
        String defaultPassword = "123456789";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptPassword = passwordEncoder.encode(defaultPassword);
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 管理员删除用户
     *
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)  // 管理员权限检查
    public BaseResponse<Boolean> deleteUser(@RequestParam Long userId, HttpServletRequest request) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.deleteUser(userId);
        return ResultUtils.success(result);
    }

    /**
     * 管理员封禁用户
     *
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("/ban")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)  // 管理员权限检查
    public BaseResponse<Boolean> banUser(@RequestParam Long userId, HttpServletRequest request) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.banUser(userId);
        return ResultUtils.success(result);
    }

    /**
     * 管理员解封用户
     *
     * @param userId
     * @param request
     * @return
     */
    @PostMapping("/unban")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)  // 管理员权限检查
    public BaseResponse<Boolean> unbanUser(@RequestParam Long userId, HttpServletRequest request) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.unbanUser(userId);
        return ResultUtils.success(result);
    }

    /**
     * 管理员更新用户
     *
     * @param userUpdateRequest
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE) // 管理员权限检查
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,
                                            HttpServletRequest request) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 根据 id 获取用户（仅管理员）
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id, HttpServletRequest request) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id, HttpServletRequest request) {
        BaseResponse<User> response = getUserById(id, request);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 分页获取用户列表（仅管理员）
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
                                                   HttpServletRequest request) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        return ResultUtils.success(userPage);
    }

    /**
     * 分页获取用户封装列表
     *
     * @param userQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page/vo")
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest,
                                                       HttpServletRequest request) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(new Page<>(current, size),
                userService.getQueryWrapper(userQueryRequest));
        Page<UserVO> userVOPage = new Page<>(current, size, userPage.getTotal());
        List<UserVO> userVO = userService.getUserVO(userPage.getRecords());
        userVOPage.setRecords(userVO);
        return ResultUtils.success(userVOPage);
    }

    /**
     * 更新个人信息
     *
     * @param userUpdateMyRequest
     * @param request
     * @return
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
                                              HttpServletRequest request) {
        if (userUpdateMyRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 重置用户密钥
     *
     * @param request
     * @return
     */
    @PostMapping("/key/reset")
    public BaseResponse<Boolean> resetUserKey(HttpServletRequest request) {
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 重置密钥
        userService.resetUserKey(loginUser.getId());
        return ResultUtils.success(true);
    }


}