# 数据库初始化

-- 创建库
create database if not exists lengapi;

-- 切换库
use lengapi;

-- 用户表
create table if not exists user
(
    id           bigint auto_increment comment 'id' primary key,
    userAccount  varchar(256)                           null comment '账号',
    userPassword varchar(512)                           not null comment '密码',
    email        varchar(256)                           null comment '邮箱',
    phone        varchar(20)                            null comment '手机号',
    userName     varchar(256)                           null comment '用户昵称',
    userAvatar   varchar(1024)                          null comment '用户头像',
    gender       tinyint                                null comment '性别（0-男, 1-女）',
    userRole     varchar(256) default 'user'            not null comment '用户角色：user/admin/ban',
    `accessKey` varchar(512) not null comment 'accessKey',
    `secretKey` varchar(512) not null comment 'secretKey',
    createTime   datetime     default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime     default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint      default 0                 not null comment '是否删除',
    UNIQUE INDEX uniq_accessKey (accessKey),  -- 添加唯一约束
    UNIQUE INDEX uniq_secretKey (secretKey),   -- 添加唯一约束
    UNIQUE INDEX uniq_email (email),   -- 添加唯一约束
    UNIQUE INDEX uniq_phone (phone)   -- 添加唯一约束
) comment '用户' collate = utf8mb4_unicode_ci;
