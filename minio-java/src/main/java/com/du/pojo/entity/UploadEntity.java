package com.du.pojo.entity;

import com.du.pojo.enums.UploadStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * [一句话描述该类的功能]
 *
 * @author : Du Yingjie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/10 15:54]
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadEntity {

    private Long id;

    /**
     * 分片数量
     */
    private Integer chunkCount;

    /**
     * 上传文件的md5
     */
    private String fileMd5;

    /**
     * 上传文件/合并文件的格式
     */
    private String suffix;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 上传状态 0.上传完成 1.已上传部分
     */
    private UploadStatusEnum uploadStatus;

    /**
     * 上传地址，内网
     */
    private String uploadUrl;

    /**
     * 上传地址，域名
     */
    private String domainName;

    /**
     * 返回给前端状态码，上传状态校验
     */
    private Integer code;

    /**
     * 分片序号
     */
    private Integer partNumber;

    /**
     * 分片初始化的时候上传文件的地址
     */
    private List<UploadEntity> chunkUploadUrls;
}
