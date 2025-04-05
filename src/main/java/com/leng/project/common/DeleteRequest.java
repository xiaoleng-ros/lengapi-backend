package com.leng.project.common;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

/**
 * 删除请求
 *
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    @Serial
    private static final long serialVersionUID = 1L;
}