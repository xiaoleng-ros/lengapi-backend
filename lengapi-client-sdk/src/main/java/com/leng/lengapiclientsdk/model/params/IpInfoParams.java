package com.leng.lengapiclientsdk.model.params;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * IP地址参数类
 */
@Data
@Accessors(chain = true)
public class IpInfoParams implements Serializable {

    @Serial
    private static final long serialVersionUID = 3815188540434269370L;

    private String ip;
}