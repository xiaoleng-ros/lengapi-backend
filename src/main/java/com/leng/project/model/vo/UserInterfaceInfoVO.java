package com.leng.project.model.vo;

import com.leng.lengapicommon.model.entity.UserInterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 接口信息封装视图
 *
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserInterfaceInfoVO extends UserInterfaceInfo {

    /**
     * 总调用次数
     */
    private Integer totalNum;

    @Serial
    private static final long serialVersionUID = 1L;
}
