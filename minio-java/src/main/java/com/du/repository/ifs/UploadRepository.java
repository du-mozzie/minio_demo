package com.du.repository.ifs;

import com.du.pojo.entity.UploadEntity;

import java.util.Optional;

/**
 * [一句话描述该类的功能]
 *
 * @author : Du Yingjie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/10 15:31]
 */
public interface UploadRepository {

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
     * 查找
     * @param id
     * @return
     */
    Optional<UploadEntity> findById(Long id);

    /**
     * 查找
     * @param fileMd5
     * @return
     */
    Optional<UploadEntity> findByMD5(String fileMd5);
}
