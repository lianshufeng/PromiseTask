package com.fast.server.promise.core.domain;

import com.fast.server.promise.core.type.MethodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Data
@Entity
@Table(name = "httpTable", indexes = {@Index(name = "httpTable_createTime", columnList = "createTime"), @Index(name = "httpTable_updateTime", columnList = "updateTime")})
@NoArgsConstructor
@AllArgsConstructor
public class HttpTable extends SuperEntity {


    @OneToOne
    private TaskTable taskTable;



    /**
     * Url地址
     */
    @Column
    private String url;


    /**
     * 网络请求方式
     */
    @Column
    private MethodType method;


    /**
     * 请求头
     */
    @Column
    private String header;


    /**
     * 请求体，仅为post生效
     */
    @Column
    private String body;


    /**
     * 请求编码
     */
    @Column
    private String charset;

}
