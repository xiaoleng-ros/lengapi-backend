package com.leng.lengapiclientsdk.model.request;

import com.leng.lengapiclientsdk.model.enums.RequestMethodEnum;
import com.leng.lengapiclientsdk.model.params.IpInfoParams;
import com.leng.lengapiclientsdk.model.response.ResultResponse;
import lombok.experimental.Accessors;

/**
 * 用户请求类
 */
@Accessors(chain = true)
public class IpInfoRequest extends BaseRequest<IpInfoParams, ResultResponse> {

    @Override
    public String getPath() {
        return "/ipInfo";
    }

    /**
     * 获取响应类
     *
     * @return {@link Class}<{@link ResultResponse}>
     */
    @Override
    public Class<ResultResponse> getResponseClass() {
        return ResultResponse.class;
    }

    @Override
    public String getMethod() {
        return RequestMethodEnum.GET.getValue();
    }

}