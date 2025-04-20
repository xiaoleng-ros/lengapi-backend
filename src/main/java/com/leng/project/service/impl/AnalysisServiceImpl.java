package com.leng.project.service.impl;

import com.leng.lengapicommon.model.vo.InterfaceInfoVO;
import com.leng.project.mapper.InterfaceInfoMapper;
import com.leng.project.service.AnalysisService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lengleng
 * @description 接口调用分析服务实现
 */
@Service
public class AnalysisServiceImpl implements AnalysisService {

    private final InterfaceInfoMapper interfaceInfoMapper;

    public AnalysisServiceImpl(InterfaceInfoMapper interfaceInfoMapper) {
        this.interfaceInfoMapper = interfaceInfoMapper;
    }

    @Override
    public List<InterfaceInfoVO> getTop3InvokeInterfaceInfo() {
        return interfaceInfoMapper.listTop3InvokeInterfaceInfo(3);
    }

    @Override
    public List<InterfaceInfoVO> getAllInterfacesInvokeCount() {
        return interfaceInfoMapper.listAllInterfaceTotal(Integer.MAX_VALUE);
    }
}