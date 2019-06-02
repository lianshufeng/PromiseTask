package com.fast.server.promise.core.model;

import com.fast.server.promise.core.type.MethodType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class HttpModel {


    /**
     * Url地址
     */
    @NotEmpty(message = "请求的url不能为空")
    private String url;


    /**
     * 网络请求方式
     */
    private MethodType methodType;


    /**
     * 请求头
     */
    private Map<String, Object> header;


    /**
     * 请求体，仅为post生效
     */
    private Map<String, Object> body;

}
