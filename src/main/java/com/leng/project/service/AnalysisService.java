package com.leng.project.service;

import com.leng.lengapicommon.model.vo.InterfaceInfoVO;
import java.util.List;

/**
 * @author lengleng
 * @description 接口调用分析服务
 */
public interface AnalysisService {

    /**
     * 获取调用次数最多的三个接口（用于 TOP3 饼图）
     *
     * @return 接口列表
     */
    List<InterfaceInfoVO> getTop3InvokeInterfaceInfo();

    /**
     * 获取所有接口的总调用次数（用于全量饼图）
     *
     * @return 接口列表
     */
    List<InterfaceInfoVO> getAllInterfacesInvokeCount();
}
