package com.leng.project.model.dto.file;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 文件上传请求
 */
@Data
public class UploadFileRequest implements Serializable {

    /**
     * 业务类型
     */
    private String biz;

    @Serial
    private static final long serialVersionUID = 1L;
}