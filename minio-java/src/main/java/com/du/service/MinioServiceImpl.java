package com.du.service;

import com.du.config.MinioProperties;
import com.du.pojo.entity.UploadEntity;
import com.du.pojo.enums.UploadStatusEnum;
import com.du.service.ifs.MinioService;
import com.du.service.ifs.UploadService;
import com.du.util.MinioUtils;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.*;

import static com.du.pojo.enums.UploadStatusEnum.UPLOAD_SUCCESS;

/**
 * @author : Du YingJie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/3 11:01]
 */
@Service
@RequiredArgsConstructor
public class MinioServiceImpl implements MinioService {

    private final MinioClient minioClient;

    private final MinioProperties minioProperties;

    private final UploadService uploadService;

    private final MinioUtils minioUtils;

    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        if (null == file || 0 == file.getSize()) {
            return null;
        }
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String fileName = minioProperties.getBucket() + "_" +
                System.currentTimeMillis() + "_" + format.format(new Date()) + "_" + new Random().nextInt(1000) +
                originalFilename.substring(originalFilename.lastIndexOf("."));
        minioClient.putObject(
                PutObjectArgs.builder().bucket(minioProperties.getBucket()).object(fileName).stream(
                                file.getInputStream(), file.getSize(), -1)
                        .contentType(file.getContentType())
                        .build());
        return minioProperties.getEndpoint() + "/" + minioProperties.getBucket() + "/" + fileName;
    }

    @Override
    public UploadEntity initChunkUpload(UploadEntity uploadEntity) {
        //????????????md5???????????????????????????
        UploadEntity upload = uploadService.findByMD5(uploadEntity.getFileMd5());
        if (upload != null) {
            //??????
            if (upload.getUploadStatus() == UPLOAD_SUCCESS) {
                upload.setCode(upload.getUploadStatus().getValue());
                return upload;
            }
            //??????
            //?????????????????????????????????
            Map<Integer, String> okChunkMap = minioUtils.mapChunkObjectNames(minioProperties.getChunkBucket(), upload.getFileMd5());
            List<UploadEntity> chunkUploadUrls = new ArrayList<>();
            if (okChunkMap.size() > 0) {
                for (int i = 1; i <= upload.getChunkCount(); i++) {
                    //??????????????????????????????????????????
                    if (!okChunkMap.containsKey(i)) {
                        //??????????????????url
                        UploadEntity url = new UploadEntity();
                        url.setPartNumber(i);
                        url.setUploadUrl(minioUtils.createUploadChunkUrl(minioProperties.getChunkBucket(), upload.getFileMd5(), i));
                        chunkUploadUrls.add(url);
                    }
                }
                if (chunkUploadUrls.size() == 0) {
                    upload.setCode(200);
                    return upload;
                }
                upload.setChunkUploadUrls(chunkUploadUrls);
                upload.setCode(upload.getUploadStatus().getValue());
                return upload;
            }
        }
        //????????????????????????????????????????????????????????????????????????????????????????????????url
        List<String> uploadUrls = minioUtils.createUploadChunkUrlList(minioProperties.getChunkBucket(), uploadEntity.getFileMd5(), uploadEntity.getChunkCount());
        List<UploadEntity> chunkUploadUrls = new ArrayList<>();
        for (int i = 1; i <= uploadUrls.size(); i++) {
            UploadEntity url = new UploadEntity();
            url.setPartNumber(i);
            url.setUploadUrl(minioUtils.createUploadChunkUrl(minioProperties.getChunkBucket(), uploadEntity.getFileMd5(), i));
            chunkUploadUrls.add(url);
        }
        uploadEntity.setChunkUploadUrls(chunkUploadUrls);
        //?????????????????????????????????????????????
        uploadEntity.setUploadStatus(UploadStatusEnum.UPLOAD_PART);
        uploadService.save(uploadEntity);
        uploadEntity.setCode(uploadEntity.getUploadStatus().getValue());
        return uploadEntity;
    }

    @Override
    public UploadEntity composeFile(UploadEntity uploadEntity) {
        //??????md5??????????????????????????????(minio??????????????? = ??????path)
        List<String> chunks = minioUtils.listObjectNames(minioProperties.getChunkBucket(), uploadEntity.getFileMd5());
        //?????????????????????
        String originalFilename = uploadEntity.getFileName();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = minioProperties.getBucket() + "_" +
                System.currentTimeMillis() + "_" + format.format(new Date()) + "_" + new Random().nextInt(1000) +
                suffix;
        //????????????
        boolean flag = minioUtils.composeObject(chunks, fileName, suffix);
        if (!flag) {
            // TODO  ?????????????????????????????????
        }
        //???????????????????????????????????????????????????????????????????????????
        UploadEntity upload = uploadService.findByMD5(uploadEntity.getFileMd5());
        upload.setUploadStatus(UPLOAD_SUCCESS);
        upload.setFileName(fileName);
        String uploadUrl = minioProperties.getEndpoint() + "/" + minioProperties.getBucket() + "/" + fileName;
        // TODO ????????????
        //String filHost = minioProperties.getFilHost() + "/" + minioProperties.getBucket() + "/" + fileName;
        upload.setUploadUrl(uploadUrl);
        upload.setSuffix(suffix);
        //upload.setFilHost();
        uploadService.update(upload);
        return upload;
    }
}