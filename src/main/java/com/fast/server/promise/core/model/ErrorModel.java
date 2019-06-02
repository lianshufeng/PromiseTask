package com.fast.server.promise.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Max;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Validated
public class ErrorModel {

    /**
     * 尝试次数
     */
    @Max(value = 20, message = "尝试最大的次数不能超过20")
    private Integer tryCount;

    /**
     * 延迟时间
     */
    private Long sleepTime;
}
