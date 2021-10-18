package com.du.pojo.dto.resp;

import com.du.pojo.entity.UploadEntity;
import com.du.pojo.enums.UploadStatusEnum;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * [一句话描述该类的功能]
 *
 * @author : Du Yingjie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/10 16:40]
 */
@Data
public class InitChunkResp {

    private Integer code;

    private String fileName;

    private String filePath;

    private String suffix;

    private UploadStatusEnum uploadStatus;

    private List<UploadEntity> chunkUploadUrls;

    public static InitChunkResp fromEntity(UploadEntity entity) {
        InitChunkResp resp = new InitChunkResp();
        BeanUtils.copyProperties(entity, resp);
        return resp;
    }
}
