package com.iscas.pm.api.model.doc;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Author： zhangchao
 * @Date： 2022/7/29
 * @Description： 文档类型枚举类
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum DocumentTypeEnum {
    LOCAL("LOCAL", "本地上传"),
    GENERATE("GENERATE", "系统生成"),
    LINK("LINK", "链接");

    private final String code;

    private final String value;

    DocumentTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
