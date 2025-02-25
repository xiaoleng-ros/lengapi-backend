use lengapi;

-- 接口信息表
create table if not exists lengapi.`interface_info`
(
    `id` bigint not null auto_increment comment '主键' primary key,
    `name` varchar(256) not null comment '接口名称',
    `description` varchar(256) null comment '接口描述',
    `url` varchar(512) not null comment '接口地址',
    `requestParams` text not null comment '请求参数',
    `requestHeader` text null comment '请求头',
    `responseHeader` text null comment '响应头',
    `status` int default 0 not null comment '接口状态(0-关闭,1-开启)',
    `method` varchar(256) not null comment '请求类型',
    `interfaceTotal` bigint default 0 not null comment '接口调用总次数',
    `userId` bigint not null comment '创建人',
    `createTime` datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    `updateTime` datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
    ) comment '接口信息';

insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`,`userId`) values ('getUserNameByPost', '获取用户名', 'http://localhost:8201/api/name/user',
                                                                                                                                                             '{"name": "username","type": "string"}', '{"content-type":"application/json"}',
                                                                                                                                                             '{"content-type": "application/json"}', 0, 'POST', 0, 1);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('邓智渊', '熊文博', '曾茂','www.marlena-kulas.org', '覃雨泽', '卢子骞', 0, '潘鹏飞', 0, 420806431);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('薛嘉懿', '罗立果', '曾茂','www.chasity-hoeger.info', '毛子轩', '董擎宇', 0, '张志强', 0, 921);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('周弘文', '黄越彬', '曾茂','www.buddy-hermiston.org', '邹文博', '洪潇然', 0, '谭鸿煊', 0, 95);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('龚思聪', '江君浩', '曾茂','www.kylie-stoltenberg.co', '林弘文', '贺泽洋', 0, '崔果', 0, 1813084);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('叶黎昕', '严天翊', '曾茂','www.angelo-sawayn.io', '杨峻熙', '金俊驰', 0, '邹弘文', 0, 6028);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('邱明哲', '高博文', '曾茂','www.many-brekke.org', '汪风华', '金鹏涛', 0, '冯烨霖', 0, 2);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('萧煜城', '崔彬', '曾茂','www.luci-romaguera.biz', '戴靖琪', '崔熠彤', 0, '张晋鹏', 0, 7);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('龙文', '武立轩', '曾茂','www.yee-homenick.org', '吴擎宇', '龙峻熙', 0, '龚鹏煊', 0, 557688);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('黎耀杰', '胡智辉', '曾茂','www.emile-swaniawski.org', '江思', '吕钰轩', 0, '刘浩宇', 0, 567438996);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('洪金鑫', '贺鹏煊', '曾茂','www.jamie-schmitt.org', '武擎苍', '汪思淼', 0, '郭明轩', 0, 1307);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('姚文博', '于鹏飞', '曾茂','www.winnifred-koelpin.io', '阎弘文', '刘博涛', 0, '刘炎彬', 0, 410546);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('罗擎苍', '杜立诚', '曾茂','www.reinaldo-hoeger.io', '卢子骞', '黄鹏涛', 0, '董明轩', 0, 7992);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('王瑾瑜', '孔雨泽', '曾茂','www.stephnie-bechtelar.info', '吕熠彤', '郭嘉懿', 0, '杜黎昕', 0, 8444);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('罗炫明', '林子骞', '曾茂','www.nathanael-spinka.name', '萧鹏', '韦文', 0, '韩伟祺', 0, 8357004);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('秦嘉熙', '金越泽', '曾茂','www.jaleesa-bins.biz', '袁黎昕', '莫果', 0, '梁金鑫', 0, 951013173);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('郭哲瀚', '陶昊然', '曾茂','www.carri-lynch.net', '孔鑫磊', '胡烨霖', 0, '尹炎彬', 0, 92375426);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('苏天磊', '顾伟祺', '曾茂','www.gale-braun.co', '任修杰', '郭烨霖', 0, '宋晟睿', 0, 93435);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('袁思远', '陆鹭洋', '曾茂','www.brice-oreilly.com', '魏文', '于志泽', 0, '梁烨磊', 0, 745276216);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('卢琪', '邓伟宸', '曾茂','www.rena-jenkins.com', '吴钰轩', '尹哲瀚', 0, '卢鹤轩', 0, 768);
insert into lengapi.`interface_info` (`name`, `description`, `url`,`requestParams`, `requestHeader`, `responseHeader`, `status`, `method`, `interfaceTotal`, `userId`) values ('沈哲瀚', '袁烨霖', '曾茂','www.keith-macejkovic.org', '萧展鹏', '李雨泽', 0, '曾炎彬', 0, 6569);

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
    `isDelete` tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'
) comment '用户调用接口关系';