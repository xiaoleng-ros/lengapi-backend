package com.leng.project.common;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * id
 */
@Data
public class IdRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    @Serial
    private static final long serialVersionUID = 1L;
}