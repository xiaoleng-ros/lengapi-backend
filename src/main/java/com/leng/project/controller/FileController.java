package com.leng.project.controller;

import cn.hutool.core.io.FileUtil;
import java.io.File;
import java.util.Arrays;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.leng.lengapicommon.model.entity.User;
import com.leng.project.common.BaseResponse;
import com.leng.project.common.ErrorCode;
import com.leng.project.common.ResultUtils;
import com.leng.project.constant.FileConstant;
import com.leng.project.exception.BusinessException;
import com.leng.project.manager.CosManager;
import com.leng.project.model.dto.file.UploadFileRequest;
import com.leng.project.model.enums.FileUploadBizEnum;
import com.leng.project.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件接口
 */
@RestController
@RequestMapping("/file")
@Slf4j
public class FileController {

    @Resource
    private UserService userService;

    @Resource
    private CosManager cosManager;

    /**
     * 文件上传
     */
    @PostMapping("/upload")
    public BaseResponse<String> uploadFile(
            @RequestParam("file") MultipartFile multipartFile,
            @RequestParam("biz") String biz,
            HttpServletRequest request) {
        // 校验业务类型
        FileUploadBizEnum fileUploadBizEnum = FileUploadBizEnum.getEnumByValue(biz);
        if (fileUploadBizEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "业务类型不支持");
        }

        // 校验文件
        validFile(multipartFile, fileUploadBizEnum);

        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR, "用户未登录");
        }

        // 生成文件名
        String timestamp = String.valueOf(System.currentTimeMillis());
        String uuid = RandomStringUtils.randomAlphanumeric(8);
        String originalFilename = multipartFile.getOriginalFilename();
        String filename = timestamp + "-" + uuid + "-" + originalFilename;

        // 生成文件路径
        String filepath = String.format("/%s/%s/%s", fileUploadBizEnum.getValue(), loginUser.getId(), filename);

        File file = null;
        try {
            // 检查文件是否为空
            if (multipartFile.isEmpty()) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件内容为空");
            }

            // 上传文件到临时目录
            file = File.createTempFile(filepath, null);
            multipartFile.transferTo(file);

            // 上传到对象存储
            cosManager.putObject(filepath, file);

            // 返回可访问地址
            String fileUrl = FileConstant.COS_HOST + filepath;

            // 如果是用户头像业务类型，更新用户头像
            if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
                userService.updateUserAvatar(loginUser.getId(), fileUrl);
            }

            return ResultUtils.success(fileUrl);
        } catch (Exception e) {
            log.error("文件上传失败，filepath = {}, error = {}", filepath, e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "上传失败");
        } finally {
            // 删除临时文件
            if (file != null && !file.delete()) {
                log.error("临时文件删除失败，filepath = {}", filepath);
            }
        }
    }

    /**
     * 校验文件
     */
    private void validFile(MultipartFile multipartFile, FileUploadBizEnum fileUploadBizEnum) {
        // 文件名为空或没有后缀
        String originalFilename = multipartFile.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件名不能为空");
        }
        String fileSuffix = FileUtil.getSuffix(originalFilename);

        // 文件大小校验
        long fileSize = multipartFile.getSize();
        final long MAX_FILE_SIZE = 1024 * 1024L * 2; // 2MB

        // 根据业务类型校验文件
        if (FileUploadBizEnum.USER_AVATAR.equals(fileUploadBizEnum)) {
            if (fileSize > MAX_FILE_SIZE) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件大小不能超过 2M");
            }
            if (!Arrays.asList("jpeg", "jpg", "png", "webp").contains(fileSuffix)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件类型错误，仅支持图片");
            }
        }
    }
}