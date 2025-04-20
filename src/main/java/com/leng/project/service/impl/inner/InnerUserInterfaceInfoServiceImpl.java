package com.leng.project.service.impl.inner;

import com.leng.lengapicommon.service.InnerUserInterfaceInfoService;
import com.leng.project.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;

    @Override
    public boolean invokeCount(long userId) {
        return userInterfaceInfoService.invokeCount(userId);
    }

}
