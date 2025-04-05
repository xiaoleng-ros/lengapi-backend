package com.leng.lengapiclientsdk.model.request;


import com.leng.lengapiclientsdk.model.enums.RequestMethodEnum;
import com.leng.lengapiclientsdk.model.params.PoisonousChickenSoupParams;
import com.leng.lengapiclientsdk.model.response.PoisonousChickenSoupResponse;
import lombok.experimental.Accessors;

/**
 *
 */
@Accessors(chain = true)
public class PoisonousChickenSoupRequest extends BaseRequest<PoisonousChickenSoupParams, PoisonousChickenSoupResponse> {

    @Override
    public String getPath() {
        return "/poisonousChickenSoup";
    }

    /**
     * 获取响应类
     *
     * @return {@link Class}<{@link PoisonousChickenSoupResponse}>
     */
    @Override
    public Class<PoisonousChickenSoupResponse> getResponseClass() {
        return PoisonousChickenSoupResponse.class;
    }

    @Override
    public String getMethod() {
        return RequestMethodEnum.GET.getValue();
    }
}
