package com.fast.server.promise.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

/**
 * 请求的模型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class RequestParmModel {


    /**
     * 任务序列化的id,不能为空
     */
    private String id;


    /**
     * 执行时间
     */
    @Max(value = 1000 * 60 * 60 * 24, message = "执行时间不能超过1天")
    private Long executeTime;


    /**
     * http的任务
     */
    @Valid
    private HttpModel http;


    /**
     * 错误模型
     */
    @Valid
    private ErrorTryModel errorTry;


}
