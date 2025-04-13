package com.leng.lengapiclientsdk.model.params;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户参数类
 */
@Data
@Accessors(chain = true)
public class NameParams implements Serializable {

    @Serial
    private static final long serialVersionUID = 3815188540434269370L;

    private String name;
}