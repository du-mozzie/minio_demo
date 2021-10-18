package com.du.service.ifs;

import com.du.pojo.entity.UploadEntity;

/**
 * [一句话描述该类的功能]
 *
 * @author : Du Yingjie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/10 16:08]
 */
public interface UploadService {

    /**
     * 保存
     * @param uploadEntity
     * @return
     */
    UploadEntity save(UploadEntity uploadEntity);

    /**
     * 删除
     * @param id
     */
    void delete(Long id);

    /**
     * 修改
     * @param uploadEntity
     * @return
     */
    UploadEntity update(UploadEntity uploadEntity);

    /**
     * 根据ID查找
     * @param id
     * @return
     */
    UploadEntity findById(Long id);

    /**
     * 根据MD5查找
     * @param fileMd5
     * @return
     */
    UploadEntity findByMD5(String fileMd5);
}
