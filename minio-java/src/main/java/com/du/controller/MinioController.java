package com.du.controller;

import com.du.pojo.dto.req.ComposeChunkReq;
import com.du.pojo.dto.req.InitChunkReq;
import com.du.pojo.dto.resp.ComposeFileResp;
import com.du.pojo.dto.resp.InitChunkResp;
import com.du.pojo.entity.UploadEntity;
import com.du.service.ifs.MinioService;
import com.fjhtxx.cloud.core.http.Resp;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author : Du YingJie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/2 17:38]
 */
@RestController
@RequestMapping("/file")
@CrossOrigin
@RequiredArgsConstructor
public class MinioController {

    private final MinioService minioService;

    @PostMapping("/upload")
    public Resp<String> uploadFile(@RequestParam MultipartFile file) throws Exception {
        return Resp.ok(minioService.uploadFile(file));
    }

    @PostMapping("/initChunk")
    public Resp<InitChunkResp> initChunkUpload(@RequestBody InitChunkReq initChunkReq) {
        UploadEntity entity = minioService.initChunkUpload(initChunkReq.toEntity());
        return Resp.ok(InitChunkResp.fromEntity(entity));
    }

    @PostMapping("/composeFile")
    public Resp<ComposeFileResp> composeFile(@RequestBody ComposeChunkReq composeChunkReq) {
        return Resp.ok(ComposeFileResp.fromEntity(minioService.composeFile(composeChunkReq.toEntity())));
    }
}
