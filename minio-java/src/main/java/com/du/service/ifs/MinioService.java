package com.du.service.ifs;

import com.du.pojo.entity.UploadEntity;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : Du YingJie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/3 11:02]
 */
public interface MinioService {

    /**
     * 文件上传
     *
     * @param file 文件
     * @return FileUploadResponse
     * @throws Exception
     */
    String uploadFile(MultipartFile file) throws Exception;

    /**
     * 分片初始化
     * @param uploadEntity uploadEntity
     * @return UploadEntity
     */
    UploadEntity initChunkUpload(UploadEntity uploadEntity);

    /**
     * 合并分片
     * @param uploadEntity uploadEntity
     * @return UploadEntity
     */
    UploadEntity composeFile(UploadEntity uploadEntity);
}