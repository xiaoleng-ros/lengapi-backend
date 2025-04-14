package com.leng.lengapiclientsdk.model.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LoveResponse extends ResultResponse {

    @Serial
    private static final long serialVersionUID = -1038984103811824271L;

    private String value;
}
