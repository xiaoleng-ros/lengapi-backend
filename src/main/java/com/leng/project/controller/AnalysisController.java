package com.leng.project.controller;

import com.leng.lengapicommon.model.vo.InterfaceInfoVO;
import com.leng.project.common.BaseResponse;
import com.leng.project.common.ResultUtils;
import com.leng.project.service.AnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分析控制器
 *
 * @author leng
 */
@RestController
@RequestMapping("/Analysis")
@Slf4j
public class AnalysisController {

    private final AnalysisService analysisService;

    public AnalysisController(AnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    // 获取调用次数最多的三个接口（用于 TOP3 饼图）
    @GetMapping("/top3/interface/invoke")
    public BaseResponse<List<InterfaceInfoVO>> getTop3InterfaceInvokeInfo() {
        List<InterfaceInfoVO> top3InterfaceInfoVOList = analysisService.getTop3InvokeInterfaceInfo();
        return ResultUtils.success(top3InterfaceInfoVOList);
    }

    // 获取所有接口的总调用次数（用于全量饼图）
    @GetMapping("/all/interfaces/invoke/count")
    public BaseResponse<List<InterfaceInfoVO>> getAllInterfacesInvokeCount() {
        List<InterfaceInfoVO> allInterfaceInfoVOList = analysisService.getAllInterfacesInvokeCount();
        return ResultUtils.success(allInterfaceInfoVOList);
    }
}