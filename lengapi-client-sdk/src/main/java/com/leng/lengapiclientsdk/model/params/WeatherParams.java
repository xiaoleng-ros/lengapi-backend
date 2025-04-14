package com.leng.lengapiclientsdk.model.params;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 获取天气请求参数
 */
@Data
@Accessors(chain = true)
public class WeatherParams implements Serializable {
    @Serial
    private static final long serialVersionUID = 3815188540434269370L;

    private String ip;

    private String city;
}
