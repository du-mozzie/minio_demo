package com.du.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author : Du YingJie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/2 11:36]
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "minio")
@EnableConfigurationProperties
public class MinioProperties {
    /**
     * 连接地址
     */
    private String endpoint;

    /**
     * 公网地址，对外暴露
     */
    private String filHost;

    /**
     * 用户名
     */
    private String accessKey;
    /**
     * 密码
     */
    private String secretKey;

    /**
     * 文件存储桶
     */
    private String bucket;

    /**
     * 分片文件临时存储桶
     */
    private String chunkBucket;

    /**
     * 分片文件url过期时间
     */
    private Integer chunkExpiry;

}
