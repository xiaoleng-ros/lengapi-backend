package com.leng.project.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leng.lengapicommon.model.entity.InterfaceInfo;

/**
* @author lengleng
* @description 针对表【interface_info(接口信息)】的数据库操作Mapper
*/
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {

    void addInterfaceTotal(Long interfaceInfoId);
}




