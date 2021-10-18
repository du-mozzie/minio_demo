package com.du.repository;

import com.du.pojo.entity.UploadEntity;
import com.du.pojo.model.QUploadDo;
import com.du.pojo.model.UploadDo;
import com.du.repository.ifs.UploadRepository;
import com.du.repository.jpa.UploadJpaRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * [一句话描述该类的功能]
 *
 * @author : Du Yingjie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/10 16:01]
 */
@Repository
@RequiredArgsConstructor
public class UploadRepositoryImpl implements UploadRepository {

    private final UploadJpaRepository uploadJpaRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public UploadEntity save(UploadEntity uploadEntity) {
        return uploadJpaRepository.save(UploadDo.fromEntity(uploadEntity)).toEntity();
    }

    @Override
    public void delete(Long id) {
        QUploadDo qUploadDo = QUploadDo.uploadDo;
        jpaQueryFactory.delete(qUploadDo)
                .where(qUploadDo.id.eq(id))
                .execute();
    }

    @Override
    public Optional<UploadEntity> findById(Long id) {
        QUploadDo qUploadDo = QUploadDo.uploadDo;
        UploadDo uploadDo = jpaQueryFactory.selectFrom(qUploadDo)
                .where(qUploadDo.id.eq(id))
                .fetchOne();
        if (uploadDo == null){
                return Optional.empty();
        }
        return Optional.of(uploadDo.toEntity());
    }

    @Override
    public Optional<UploadEntity> findByMD5(String fileMd5) {
        QUploadDo qUploadDo = QUploadDo.uploadDo;
        UploadDo uploadDo = jpaQueryFactory.selectFrom(qUploadDo)
                .where(qUploadDo.fileMd5.eq(fileMd5))
                .fetchOne();
        if (uploadDo == null){
            return Optional.empty();
        }
        return Optional.of(uploadDo.toEntity());
    }
}
