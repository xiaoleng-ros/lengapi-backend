package com.leng.lengapicommon.service;

/**
 * 内部用户接口信息服务
 *
 */
public interface InnerUserInterfaceInfoService {

    /**
     * 调用次数+1
     * @param userId
     * @return
     */
    boolean invokeCount(long userId);
}
