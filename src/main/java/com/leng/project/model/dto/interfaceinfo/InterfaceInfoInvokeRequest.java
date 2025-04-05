package com.leng.project.model.dto.interfaceinfo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 接口调用请求
 *
 */
@Data
public class InterfaceInfoInvokeRequest implements Serializable {

    /**
     * 主键
     */
    private Long id;

    /**
     *
     */
    private List<Field> requestParams;

    /**
     * 用户请求参数
     */
    private String userRequestParams;

    @Serial
    private static final long serialVersionUID = 1L;

    @Data
    public static class Field {
        private String fieldName;
        private String value;
    }
}