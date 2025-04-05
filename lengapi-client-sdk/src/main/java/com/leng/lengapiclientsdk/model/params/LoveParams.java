package com.leng.lengapiclientsdk.model.params;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 随机一句情话
 */
@Data
@Accessors(chain = true)
public class LoveParams implements Serializable {

    @Serial
    private static final long serialVersionUID = 3815188540434269370L;
}
