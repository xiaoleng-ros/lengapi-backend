package com.leng.project.model.vo;

import com.leng.lengapicommon.model.entity.InterfaceInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * 接口信息封装视图
 *
 */
@Data
public class InterfaceInfoVO implements Serializable {

    /**
     * 接口 ID
     */
    private Long id;

    /**
     * 接口名称
     */
    private String name;

    /**
     * 接口总调用次数
     */
    private Integer interfaceTotal;

    @Serial
    private static final long serialVersionUID = 1L;
}
