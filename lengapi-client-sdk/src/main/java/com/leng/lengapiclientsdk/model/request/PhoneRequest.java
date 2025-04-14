package com.leng.lengapiclientsdk.model.request;


import com.leng.lengapiclientsdk.model.enums.RequestMethodEnum;
import com.leng.lengapiclientsdk.model.params.PhoneParams;
import com.leng.lengapiclientsdk.model.response.NameResponse;
import com.leng.lengapiclientsdk.model.response.ResultResponse;
import lombok.experimental.Accessors;

/**
 * 获取手机号归属地请求
 */
@Accessors(chain = true)
public class PhoneRequest extends BaseRequest<PhoneParams, ResultResponse> {

    @Override
    public String getPath() {
        return "/phone";
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
