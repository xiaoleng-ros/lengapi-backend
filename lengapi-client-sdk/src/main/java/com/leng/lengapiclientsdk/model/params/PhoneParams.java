package com.leng.lengapiclientsdk.model.params;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 获取手机号归属地请求参数
 */
@Data
@Accessors(chain = true)
public class PhoneParams implements Serializable {

    @Serial
    private static final long serialVersionUID = 3815188540434269370L;

    private String phone;

}
