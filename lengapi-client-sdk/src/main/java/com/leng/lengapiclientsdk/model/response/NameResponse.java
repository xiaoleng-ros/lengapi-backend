package com.leng.lengapiclientsdk.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * 用户响应类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class NameResponse extends ResultResponse {

    @Serial
    private static final long serialVersionUID = 1L;

    private String username;
}