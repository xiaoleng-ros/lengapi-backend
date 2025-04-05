package com.leng.lengapiclientsdk.model.request;


import com.leng.lengapiclientsdk.model.enums.RequestMethodEnum;
import com.leng.lengapiclientsdk.model.response.LoveResponse;
import lombok.experimental.Accessors;

/**
 * 随机情话
 */
@Accessors(chain = true)
public class LoveRequest extends BaseRequest<String, LoveResponse> {

    @Override
    public String getPath() {
        return "/loveTalk";
    }

    /**
     * 获取响应类
     *
     * @return {@link Class}<{@link LoveResponse}>
     */
    @Override
    public Class<LoveResponse> getResponseClass() {
        return LoveResponse.class;
    }

    @Override
    public String getMethod() {
        return RequestMethodEnum.GET.getValue();
    }
}
