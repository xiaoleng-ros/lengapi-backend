package com.leng.project.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.leng.lengapicommon.model.entity.InterfaceInfo;

/**
* @author lengleng
* @description 针对表【interface_info(接口信息)】的数据库操作Service
*/
public interface InterfaceInfoService extends IService<InterfaceInfo> {

     /**
      * 添加接口信息
      * @param interfaceInfo
      * @param add
      */
     void validInterfaceInfo(InterfaceInfo interfaceInfo, boolean add);

}
