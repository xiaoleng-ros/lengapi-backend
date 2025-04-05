package com.leng.lengapiclientsdk.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PoisonousChickenSoupResponse extends ResultResponse {

    @Serial
    private static final long serialVersionUID = -6467312483425078539L;

    private String text;
}
