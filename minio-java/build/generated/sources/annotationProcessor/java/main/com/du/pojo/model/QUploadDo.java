package com.du.pojo.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QUploadDo is a Querydsl query type for UploadDo
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QUploadDo extends EntityPathBase<UploadDo> {

    private static final long serialVersionUID = -1865416309L;

    public static final QUploadDo uploadDo = new QUploadDo("uploadDo");

    public final NumberPath<Integer> chunkCount = createNumber("chunkCount", Integer.class);

    public final StringPath domainName = createString("domainName");

    public final StringPath fileMd5 = createString("fileMd5");

    public final StringPath fileName = createString("fileName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath suffix = createString("suffix");

    public final EnumPath<com.du.pojo.enums.UploadStatusEnum> uploadStatus = createEnum("uploadStatus", com.du.pojo.enums.UploadStatusEnum.class);

    public final StringPath uploadUrl = createString("uploadUrl");

    public QUploadDo(String variable) {
        super(UploadDo.class, forVariable(variable));
    }

    public QUploadDo(Path<? extends UploadDo> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUploadDo(PathMetadata metadata) {
        super(UploadDo.class, metadata);
    }

}

