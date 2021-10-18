package com.du.pojo.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fjhtxx.cloud.core.converter.AbstractPersistentEnumConverter;
import com.fjhtxx.cloud.core.enums.PersistentEnum;

import java.util.stream.Stream;

/**
 * [一句话描述该类的功能]
 *
 * @author : Du Yingjie (2548425238@qq.com)
 * @version : [v1.0]
 * @date : [2021/9/10 15:27]
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UploadStatusEnum implements PersistentEnum<Integer> {

    /**
     * 上传完成
     */
    UPLOAD_SUCCESS(0, "上传完成"),

    /**
     * 部分上传
     */
    UPLOAD_PART(1, "部分上传");

    private final Integer value;
    private final String label;

    UploadStatusEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    @JsonCreator
    public static UploadStatusEnum from(Integer value) {
        return Stream.of(values())
                .filter(e -> e.getValue().equals(value))
                .findFirst()
                .get();
    }

    @Override
    public Integer getValue() {
        return value;
    }

    public String getLabel() {
        return label;
    }

    public static class Converter extends AbstractPersistentEnumConverter<UploadStatusEnum, Integer> {
        protected Converter() {
            super(UploadStatusEnum.class);
        }
    }

}
