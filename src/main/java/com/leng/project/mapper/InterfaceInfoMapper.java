package com.leng.project.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.leng.lengapicommon.model.entity.InterfaceInfo;
import com.leng.lengapicommon.model.vo.InterfaceInfoVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author lengleng
 * @description 针对表【interface_info(接口信息)】的数据库操作Mapper
 */
public interface InterfaceInfoMapper extends BaseMapper<InterfaceInfo> {

    /**
     * 更新接口调用总次数
     *
     * @param id 接口信息ID
     */
    void addInterfaceTotal(@Param("id") Long id);

    /**
     * 查询调用次数最多的三个接口（用于 TOP3 饼图）
     *
     * @param limit 查询的接口数量
     * @return 接口列表
     */
    List<InterfaceInfoVO> listTop3InvokeInterfaceInfo(@Param("limit") int limit);

    /**
     * 查询所有接口的总调用次数（用于全量饼图）
     *
     * @param limit 查询的接口数量（可以设置为一个较大的值，如 Integer.MAX_VALUE，以获取所有接口）
     * @return 接口列表
     */
    List<InterfaceInfoVO> listAllInterfaceTotal(@Param("limit") int limit);
}