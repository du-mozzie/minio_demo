package com.du.pojo.dto.resp;

import com.du.pojo.entity.UploadEntity;
import lombok.Data;
import org.springframework.beans.BeanUtils;

/**
 * [一句话描述该类的功能]
 *
 * @author : Du Yingjie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/10 17:07]
 */
@Data
public class ComposeFileResp {

    /**
     * 上传地址，内网地址
     */
    private String uploadUrl;

    //TODO
    /**
     * 公网地址，对外暴露
     */
    private String domainName;

    public static ComposeFileResp fromEntity(UploadEntity entity) {
        ComposeFileResp resp = new ComposeFileResp();
        BeanUtils.copyProperties(entity, resp);
        return resp;
    }

}
