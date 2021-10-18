package com.du.util;

import com.du.config.MinioProperties;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * [一句话描述该类的功能]
 *
 * @author : Du Yingjie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/12 12:20]
 */
@Component
@RequiredArgsConstructor
public class MinioUtils {

    private final MinioClient minioClient;

    private final MinioProperties minioProperties;

    /**
     * 存储桶是否存在
     *
     * @param bucketName 存储桶名称
     * @return true/false
     */
    @SneakyThrows
    public boolean isBucketExist(String bucketName) {
        return minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
    }

    /**
     * 创建存储桶
     *
     * @param bucketName 存储桶名称
     * @return true/false
     */
    @SneakyThrows
    public boolean createBucket(String bucketName) {
        minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        return true;
    }

    /**
     * 获取访问对象的外链地址
     *
     * @param bucketName 存储桶名称
     * @param objectName 对象名称
     * @param expiry     过期时间(分钟) 最大为7天 超过7天则默认最大值
     * @return viewUrl
     */
    @SneakyThrows
    public String getObjectUrl(String bucketName, String objectName, Integer expiry) {
        if (bucketName == null) {
            bucketName = minioProperties.getBucket();
        }
        expiry = expiryHandle(expiry);
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(expiry)
                        .build()
        );
    }

    /**
     * 创建上传文件对象的外链
     *
     * @param bucketName 存储桶名称
     * @param objectName 欲上传文件对象的名称
     * @param expiry     过期时间(分钟) 最大为7天 超过7天则默认最大值
     * @return uploadUrl
     */
    @SneakyThrows
    public String createUploadUrl(String bucketName, String objectName, Integer expiry) {
        expiry = expiryHandle(expiry);
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(expiry)
                        .build()
        );
    }

    /**
     * 创建上传文件对象的外链
     *
     * @param bucketName 存储桶名称
     * @param objectName 欲上传文件对象的名称
     * @return uploadUrl
     */
    public String createUploadUrl(String bucketName, String objectName) {
        return createUploadUrl(bucketName, objectName, minioProperties.getChunkExpiry());
    }

    /**
     * 批量创建分片上传外链
     *
     * @param bucketName 存储桶名称
     * @param objectMD5  欲上传分片文件主文件的MD5
     * @param chunkCount 分片数量
     * @return uploadChunkUrls
     */
    public List<String> createUploadChunkUrlList(String bucketName, String objectMD5, Integer chunkCount) {
        if (null == bucketName) {
            bucketName = minioProperties.getChunkBucket();
        }
        if (null == objectMD5) {
            return null;
        }
        objectMD5 += "/";
        if (null == chunkCount || 0 == chunkCount) {
            return null;
        }
        List<String> urlList = new ArrayList<>(chunkCount);
        for (int i = 1; i <= chunkCount; i++) {
            String objectName = objectMD5 + i + ".chunk";
            urlList.add(createUploadUrl(bucketName, objectName, minioProperties.getChunkExpiry()));
        }
        return urlList;
    }

    /**
     * 创建指定序号的分片文件上传外链
     *
     * @param chunkBucket 存储桶名称
     * @param objectMD5   上传分片文件主文件的MD5
     * @param partNumber  分片序号
     * @return uploadChunkUrl
     */
    public String createUploadChunkUrl(String chunkBucket, String objectMD5, Integer partNumber) {
        if (null == chunkBucket) {
            chunkBucket = minioProperties.getChunkBucket();
        }
        if (null == objectMD5) {
            return null;
        }
        objectMD5 += "/" + partNumber + ".chunk";
        return createUploadUrl(chunkBucket, objectMD5, minioProperties.getChunkExpiry());
    }

    /**
     * 获取对象文件名称列表
     *
     * @param bucketName 存储桶名称
     * @param prefix     对象名称前缀
     * @param sort       是否排序(升序)
     * @return objectNames
     */
    @SneakyThrows
    public List<String> listObjectNames(String bucketName, String prefix, Boolean sort) {
        ListObjectsArgs listObjectsArgs;
        if (null == prefix) {
            listObjectsArgs = ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .recursive(true)
                    .build();
        } else {
            listObjectsArgs = ListObjectsArgs.builder()
                    .bucket(bucketName)
                    .prefix(prefix)
                    .recursive(true)
                    .build();
        }
        Iterable<Result<Item>> chunks = minioClient.listObjects(listObjectsArgs);
        List<String> chunkPaths = new ArrayList<>();
        for (Result<Item> item : chunks) {
            chunkPaths.add(item.get().objectName());
        }
        if (sort) {
            return chunkPaths.stream().distinct().collect(Collectors.toList());
        }
        return chunkPaths;
    }

    /**
     * 获取对象文件名称列表
     *
     * @param bucketName 存储桶名称
     * @param prefix     对象名称前缀
     * @return objectNames
     */
    public List<String> listObjectNames(String bucketName, String prefix) {
        return listObjectNames(bucketName, prefix, false);
    }

    /**
     * 获取分片文件名称列表
     *
     * @param bucketName 存储桶名称
     * @param ObjectMd5  对象Md5
     * @return objectChunkNames
     */
    public List<String> listChunkObjectNames(String bucketName, String ObjectMd5) {
        if (null == bucketName) {
            bucketName = minioProperties.getBucket();
        }
        if (null == ObjectMd5) {
            return null;
        }
        return listObjectNames(bucketName, ObjectMd5, true);
    }

    /**
     * 获取分片名称地址HashMap key=分片序号 value=分片文件地址
     *
     * @param chunkBucket 存储桶名称
     * @param ObjectMd5   对象Md5
     * @return objectChunkNameMap
     */
    public Map<Integer, String> mapChunkObjectNames(String chunkBucket, String ObjectMd5) {
        if (null == chunkBucket) {
            chunkBucket = minioProperties.getChunkBucket();
        }
        if (null == ObjectMd5) {
            return null;
        }
        List<String> chunkPaths = listObjectNames(chunkBucket, ObjectMd5);
        if (null == chunkPaths || chunkPaths.size() == 0) {
            return null;
        }
        Map<Integer, String> chunkMap = new HashMap<>(chunkPaths.size());
        for (String chunkName : chunkPaths) {
            Integer partNumber = Integer.parseInt(chunkName.substring(chunkName.indexOf("/") + 1, chunkName.lastIndexOf(".")));
            chunkMap.put(partNumber, chunkName);
        }
        return chunkMap;
    }

    /**
     * 合并分片文件成对象文件
     *
     * @param chunkNames 分片文件名称集合
     * @param objectName 合并后的对象文件名称
     * @param suffix     文件后缀
     * @return true/false
     */
    @SneakyThrows
    public boolean composeObject(List<String> chunkNames, String objectName, String suffix) {
        Map<String, String> headers = new HashMap<>(16);
        switch (suffix.toLowerCase()) {
            case ".mp4":
                headers.put("content-type", "video/mp4");
            default:
        }
        List<ComposeSource> sourceObjectList = new ArrayList<>(chunkNames.size());
        for (String chunk : chunkNames) {
            sourceObjectList.add(
                    ComposeSource.builder()
                            .bucket(minioProperties.getChunkBucket())
                            .object(chunk)
                            .build()
            );
        }
        minioClient.composeObject(
                ComposeObjectArgs.builder()
                        .bucket(minioProperties.getBucket())
                        .object(objectName)
                        .sources(sourceObjectList)
                        .headers(headers)
                        .build()
        );
        return true;
    }

    /**
     * 将分钟数转换为秒数
     *
     * @param expiry 过期时间(分钟数)
     * @return expiry
     */
    private int expiryHandle(Integer expiry) {
        expiry = expiry * 60;
        if (expiry > 604800) {
            return 604800;
        }
        return expiry;
    }
}
