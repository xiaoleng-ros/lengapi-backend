package com.leng.lengapiclientsdk.model.request;

import com.leng.lengapiclientsdk.model.enums.RequestMethodEnum;
import com.leng.lengapiclientsdk.model.params.NameParams;
import com.leng.lengapiclientsdk.model.response.NameResponse;

import lombok.experimental.Accessors;

/**
 * 用户请求类
 */
@Accessors(chain = true)
public class NameRequest extends BaseRequest<NameParams, NameResponse> {

    @Override
    public String getPath() {
        return "/name";
    }

    /**
     * 获取响应类
     *
     * @return {@link Class}<{@link NameResponse}>
     */
    @Override
    public Class<NameResponse> getResponseClass() {
        return NameResponse.class;
    }

    @Override
    public String getMethod() {
        return RequestMethodEnum.GET.getValue();
    }

}