package com.du.pojo.dto.req;

import com.du.pojo.entity.UploadEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * [一句话描述该类的功能]
 *
 * @author : Du Yingjie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/12 13:36]
 */
@Data
public class ComposeChunkReq {

    /**
     * 上传文件的md5
     */
    private String fileMd5;

    /**
     * 文件名称
     */
    private String fileName;

    public UploadEntity toEntity() {
        UploadEntity entity = new UploadEntity();
        BeanUtils.copyProperties(this, entity);
        return entity;
    }

}
