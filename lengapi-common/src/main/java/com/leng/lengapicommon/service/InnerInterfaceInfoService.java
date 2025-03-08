package com.leng.lengapicommon.service;

import com.leng.lengapicommon.model.entity.InterfaceInfo;

/**
 * 内部接口信息服务
 *
 */
public interface InnerInterfaceInfoService {

    /**
     * 获取接口信息
     * @param path 请求路径
     * @param method 请求方法
     * @return 接口信息
     */
    InterfaceInfo getInterfaceInfo(String path, String method);

    /**
     * 更新接口总调用次数
     * @param interfaceInfoId 接口ID
     */
    void addInterfaceTotal(Long interfaceInfoId);
}
