package com.du.repository.jpa;

import com.du.pojo.model.UploadDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * [一句话描述该类的功能]
 *
 * @author : Du Yingjie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/10 15:56]
 */
public interface UploadJpaRepository extends JpaRepository<UploadDo, Long>, QuerydslPredicateExecutor<UploadDo> {
}
