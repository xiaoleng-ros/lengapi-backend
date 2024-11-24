use lengapi;

-- 接口信息表
create table if not exists lengapi.`interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `name` varchar(256) not null comment '名称',
    `description` varchar(256) null comment '描述',
    `url` varchar(512) not null comment '接口地址',
    `requestParams` text not null comment '请求参数',
    `requestHeader` text null comment '请求头',
    `responseHeader` text null comment '响应头',
    `status` int default 0 not null comment '接口状态(0-关闭,1-开启)',
    `method` varchar(256) not null comment '请求类型',
    `userId` bigint not null comment '创建人',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDeleted` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
    ) comment '接口信息';

insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('邓智渊', '熊文博', 'www.marlena-kulas.org', '覃雨泽', '卢子骞', 0, '潘鹏飞', 420806431);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('薛嘉懿', '罗立果', 'www.chasity-hoeger.info', '毛子轩', '董擎宇', 0, '张志强', 921);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('周弘文', '黄越彬', 'www.buddy-hermiston.org', '邹文博', '洪潇然', 0, '谭鸿煊', 95);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('龚思聪', '江君浩', 'www.kylie-stoltenberg.co', '林弘文', '贺泽洋', 0, '崔果', 1813084);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('叶黎昕', '严天翊', 'www.angelo-sawayn.io', '杨峻熙', '金俊驰', 0, '邹弘文', 6028);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('邱明哲', '高博文', 'www.many-brekke.org', '汪风华', '金鹏涛', 0, '冯烨霖', 2);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('萧煜城', '崔彬', 'www.luci-romaguera.biz', '戴靖琪', '崔熠彤', 0, '张晋鹏', 7);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('龙文', '武立轩', 'www.yee-homenick.org', '吴擎宇', '龙峻熙', 0, '龚鹏煊', 557688);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('黎耀杰', '胡智辉', 'www.emile-swaniawski.org', '江思', '吕钰轩', 0, '刘浩宇', 567438996);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('洪金鑫', '贺鹏煊', 'www.jamie-schmitt.org', '武擎苍', '汪思淼', 0, '郭明轩', 1307);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('姚文博', '于鹏飞', 'www.winnifred-koelpin.io', '阎弘文', '刘博涛', 0, '刘炎彬', 410546);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('罗擎苍', '杜立诚', 'www.reinaldo-hoeger.io', '卢子骞', '黄鹏涛', 0, '董明轩', 7992);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('王瑾瑜', '孔雨泽', 'www.stephnie-bechtelar.info', '吕熠彤', '郭嘉懿', 0, '杜黎昕', 8444);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('罗炫明', '林子骞', 'www.nathanael-spinka.name', '萧鹏', '韦文', 0, '韩伟祺', 8357004);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('秦嘉熙', '金越泽', 'www.jaleesa-bins.biz', '袁黎昕', '莫果', 0, '梁金鑫', 951013173);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('郭哲瀚', '陶昊然', 'www.carri-lynch.net', '孔鑫磊', '胡烨霖', 0, '尹炎彬', 92375426);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('苏天磊', '顾伟祺', 'www.gale-braun.co', '任修杰', '郭烨霖', 0, '宋晟睿', 93435);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('袁思远', '陆鹭洋', 'www.brice-oreilly.com', '魏文', '于志泽', 0, '梁烨磊', 745276216);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('卢琪', '邓伟宸', 'www.rena-jenkins.com', '吴钰轩', '尹哲瀚', 0, '卢鹤轩', 768);
insert into lengapi.`interface_info` (`name`, `description`, `url`, `requestHeader`, `responseHeader`, `status`, `method`, `userId`) values ('沈哲瀚', '袁烨霖', 'www.keith-macejkovic.org', '萧展鹏', '李雨泽', 0, '曾炎彬', 6569);

-- 用户调用接口关系表
create table if not exists lengapi.`user_interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `userId` bigint not null comment '调用用户 id',
    `interfaceInfoId` bigint not null comment '接口 id',
    `totalNum` int default 0 not null comment '总调用次数',
    `leftNum` int default 0 not null comment '剩余调用次数',
    `status` int default 0 not null comment '0-正常,1-禁用',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDeleted` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户调用接口关系';