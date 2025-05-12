package com.leng.project.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leng.lengapicommon.model.entity.User;
import com.leng.project.common.ErrorCode;
import com.leng.project.constant.CommonConstant;
import com.leng.project.exception.BusinessException;
import com.leng.project.mapper.UserMapper;
import com.leng.project.model.dto.user.UserQueryRequest;
import com.leng.project.model.enums.UserRoleEnum;
import com.leng.project.model.vo.LoginUserVO;
import com.leng.project.model.vo.UserVO;
import com.leng.project.service.UserService;
import com.leng.project.utils.JwtUtils;
import com.leng.project.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 用户注册
     */
    @Override
    public long userRegister(String userName, String userAccount, String userPassword, String checkPassword) {
        // 校验参数
        if (StringUtils.isAnyBlank(userName, userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userName.length() > 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户昵称过长");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (userAccount.intern()) {
            // 检查账号是否重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 账号不能重复，包括已删除的账号
            if (checkUserAccountExists(userAccount)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "该账号已被注册");
            }
            // 加密密码
            String encryptPassword = passwordEncoder.encode(userPassword);
            // 生成 accessKey 和 secretKey
            // 使用更安全的生成方式（SHA256+UUID盐值）
            String salt = IdUtil.fastSimpleUUID(); // 生成UUID作为盐值
            String accessKey = SecureUtil.sha256(userAccount + System.currentTimeMillis() + salt);
            String secretKey = SecureUtil.sha256(salt + System.nanoTime() + userAccount);
            // 插入用户数据
            User user = new User();
            user.setUserName(userName);
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            user.setAccessKey(accessKey);
            user.setSecretKey(secretKey);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        }
    }

    /**
     * 用户登录
     */
    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword) {
        // 校验参数
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 验证用户和密码
        if (user == null || !passwordEncoder.matches(userPassword, user.getUserPassword())) {
            log.info("用户登录失败，账号或密码错误");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        // 判断用户是否被封禁
        if (UserRoleEnum.BAN.getValue().equals(user.getUserRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "您的账号已被封禁，无法登录");
        }
        // 生成 JWT 令牌
        String token = JwtUtils.generateToken(userAccount);
        // 返回登录用户信息和令牌
        LoginUserVO loginUserVO = this.getLoginUserVO(user);
        loginUserVO.setToken(token);
        return loginUserVO;
    }

    /**
     * 获取当前登录用户
     */
    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 获取 JWT 令牌
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // 验证令牌
        String userAccount = JwtUtils.validateToken(token);
        if (userAccount == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }
        // 查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        User user = this.baseMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户不存在");
        }
        return user;
    }

    /**
     * 获取当前登录用户（允许未登录）
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 获取 JWT 令牌
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        // 验证令牌
        String userAccount = JwtUtils.validateToken(token);
        if (userAccount == null) {
            return null;
        }
        // 查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        return this.baseMapper.selectOne(queryWrapper);
    }

    /**
     * 是否为管理员
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        User user = getLoginUserPermitNull(request);
        return isAdmin(user);
    }

    /**
     * 是否为管理员
     */
    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 用户退出
     *
     * @param request HTTP请求
     * @return 是否成功退出
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 获取当前登录用户
        User loginUser = getLoginUserPermitNull(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }
        try {
            // 获取JWT令牌
            String token = request.getHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                // 将令牌加入黑名单或设置为失效
                JwtUtils.invalidateToken(token);
            }
            // 清除会话信息
            request.getSession().invalidate();
            // 记录用户退出日志
            log.info("用户 {} 退出登录，IP: {}", loginUser.getUserAccount(), request.getRemoteAddr());
            return true;
        } catch (Exception e) {
            log.error("用户退出异常", e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "退出失败");
        }
    }

    /**
     * 获取登录用户视图对象
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    /**
     * 获取用户视图对象
     */
    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    /**
     * 获取用户视图对象列表
     */
    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    /**
     * 获取查询条件
     */
    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StringUtils.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StringUtils.isNotBlank(userName), "userName", userName);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }

    /**
     * 用户头像上传
     */
    @Override
    public void updateUserAvatar(Long id, String fileUrl) {
        // 1.校验参数
        if (id == null || StringUtils.isBlank(fileUrl)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        // 2.校验头像 URL 格式
        if (!fileUrl.startsWith("http://") && !fileUrl.startsWith("https://")) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "头像 URL 格式错误");
        }
        // 3.查询用户
        User user = this.getById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        // 4.更新头像
        user.setUserAvatar(fileUrl);
        boolean updateResult = this.updateById(user);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新头像失败");
        }
    }

    /**
     * 重置用户密钥
     */
    @Override
    public void resetUserKey(Long userId) {
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        
        // 生成新的密钥对
        String salt = IdUtil.fastSimpleUUID(); // 生成UUID作为盐值
        String accessKey = SecureUtil.sha256(user.getUserAccount() + System.currentTimeMillis() + salt);
        String secretKey = SecureUtil.sha256(salt + System.nanoTime() + user.getUserAccount());
        
        // 更新用户密钥
        user.setAccessKey(accessKey);
        user.setSecretKey(secretKey);
        boolean result = this.updateById(user);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "重置密钥失败");
        }
    }

    /**
     * 删除用户
     */
    @Override
    public boolean deleteUser(Long userId) {
        if (userId == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户id不能为空");
        }
        // 判断用户是否存在
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        // 执行删除
        return this.removeById(userId);
    }

    /**
     * 封禁用户
     */
    @Override
    public boolean banUser(Long userId) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        user.setId(userId);
        user.setUserRole(UserRoleEnum.BAN.getValue());
        return this.updateById(user);
    }

    /**
     * 解封用户
     */
    @Override
    public boolean unbanUser(Long userId) {
        if (userId == null || userId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户ID不能为空或小于等于0");
        }
        // 判断用户是否存在
        User user = this.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }
        // 判断用户当前是否处于封禁状态
        if (!UserRoleEnum.BAN.getValue().equals(user.getUserRole())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "该用户未被封禁");
        }
        // 将用户角色设置回普通用户
        user.setUserRole(UserRoleEnum.USER.getValue());
        return this.updateById(user);
    }

    /**
     * 检查用户账号是否存在
     */
    public boolean checkUserAccountExists(String userAccount) {
        // 创建查询条件，包含已删除的记录
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        // 设置包含已删除记录
        queryWrapper.select("id", "isDelete");

        User user = baseMapper.selectOne(queryWrapper);
        if (user != null) {
            // 如果找到记录，无论是否删除都返回true
            return true;
        }
        return false;
    }


}


