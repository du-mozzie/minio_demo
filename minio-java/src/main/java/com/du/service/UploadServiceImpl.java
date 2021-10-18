package com.du.service;

import com.du.pojo.entity.UploadEntity;
import com.du.repository.ifs.UploadRepository;
import com.du.service.ifs.UploadService;
import com.fjhtxx.cloud.core.utils.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * [一句话描述该类的功能]
 *
 * @author : Du Yingjie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/10 16:08]
 */
@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    private final UploadRepository uploadRepository;

    @Override
    public UploadEntity save(UploadEntity uploadEntity) {
        return uploadRepository.save(uploadEntity);
    }

    @Override
    public void delete(Long id) {
        uploadRepository.delete(id);
    }

    @Override
    public UploadEntity update(UploadEntity uploadEntity) {
        UploadEntity entity = findById(uploadEntity.getId());
        ObjectUtil.copyNonNullProperties(uploadEntity, entity);
        return uploadRepository.save(entity);
    }

    @Override
    public UploadEntity findById(Long id) {
        UploadEntity entity = uploadRepository.findById(id).orElse(null);
        assert entity != null;
        return entity;
    }

    @Override
    public UploadEntity findByMD5(String fileMd5) {
        UploadEntity entity = uploadRepository.findByMD5(fileMd5).orElse(null);
        assert entity != null;
        return entity;
    }
}
