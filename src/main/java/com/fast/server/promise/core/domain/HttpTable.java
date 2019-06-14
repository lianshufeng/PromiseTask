package com.fast.server.promise.core.domain;

import com.fast.server.promise.core.type.MethodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HttpTable extends SuperEntity {


    @DBRef(lazy = true)
    private TaskTable taskTable;


    /**
     * Url地址
     */
    @Indexed
    private String url;


    /**
     * 网络请求方式
     */
    @Indexed
    private MethodType method;


    /**
     * 请求头
     */
    @Indexed
    private String header;


    /**
     * 请求体，仅为post生效
     */
    @Indexed
    private String body;


    /**
     * 请求编码
     */
    @Indexed
    private String charset;

}
