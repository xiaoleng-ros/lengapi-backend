package com.leng.project.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leng.lengapicommon.model.entity.UserInterfaceInfo;



public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {


     void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

     /**
      * 调用接口统计
      * @param userId
      * @return
      */
     boolean invokeCount(long userId);
}
