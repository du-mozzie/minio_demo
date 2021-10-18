package com.du.pojo.model;

import cn.hutool.core.bean.BeanUtil;
import com.du.pojo.enums.UploadStatusEnum;
import com.du.pojo.entity.UploadEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Table;

import javax.persistence.*;
import java.io.Serializable;


/**
 * [一句话描述该类的功能]
 *
 * @author : Du Yingjie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/10 15:13]
 */
@Data
@Entity(name = "upload")
@Table(appliesTo = "upload")
@EqualsAndHashCode(callSuper = false)
public class UploadDo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 分片数量
     */
    @Column(columnDefinition = "INTEGER COMMENT '分片数量'")
    private Integer chunkCount;

    /**
     * 上传文件的md5
     */
    @Column(columnDefinition = "VARCHAR(512) COMMENT 'MD5'")
    private String fileMd5;

    /**
     * 上传文件/合并文件的格式
     */
    @Column(columnDefinition = "VARCHAR(512) COMMENT '文件格式'")
    private String suffix;

    /**
     * 文件名称
     */
    @Column(columnDefinition = "VARCHAR(512) COMMENT '文件名称'")
    private String fileName;

    /**
     * 上传状态 0.上传完成 1.已上传部分
     */
    @Column(columnDefinition = "TINYINT COMMENT '上传状态'")
    private UploadStatusEnum uploadStatus;

    /**
     * 上传地址
     */
    @Column(columnDefinition = "VARCHAR(512) COMMENT '上传地址'")
    private String uploadUrl;

    /**
     * 公网地址
     */
    @Column(columnDefinition = "VARCHAR(512) COMMENT '域名'")
    private String domainName;

    public static UploadDo fromEntity(UploadEntity entity) {
        UploadDo uploadDo = new UploadDo();
        BeanUtil.copyProperties(entity, uploadDo);
        return uploadDo;
    }

    public UploadEntity toEntity() {
        UploadEntity entity = new UploadEntity();
        BeanUtil.copyProperties(this, entity);
        return entity;
    }

}
