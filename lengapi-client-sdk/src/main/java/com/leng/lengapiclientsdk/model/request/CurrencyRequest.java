package com.leng.lengapiclientsdk.model.request;


import com.leng.lengapiclientsdk.model.response.ResultResponse;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 通用请求
 */
@Data
@Accessors(chain = true)
public class CurrencyRequest extends BaseRequest<Object, ResultResponse> {
    private String method;
    private String path;

    /**
     * get方法
     *
     * @return {@link String}
     */
    @Override
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * 获取路径
     *
     * @return {@link String}
     */
    @Override
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 设置请求参数
     * 
     * @param params 参数对象
     */
    @Override
    public void setRequestParams(Object params) {
        super.setRequestParams(params);
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
}
