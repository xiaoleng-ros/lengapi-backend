package com.leng.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leng.lengapicommon.model.entity.UserInterfaceInfo;

import java.util.List;

/**
 * 用户调用接口关系 Mapper
 * @author leng
 */
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {

    void updateLeftNum(Long interfaceInfoId);

    List<UserInterfaceInfo> getUserLeftInvokeTimes(int limit);
}




