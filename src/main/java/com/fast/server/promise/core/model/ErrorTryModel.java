package com.fast.server.promise.core.model;

import com.fast.server.promise.core.type.CheckType;
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
public class ErrorTryModel {

    /**
     * 尝试次数
     */
    @Max(value = 99, message = "尝试最大的次数不能超过99")
    private Integer tryCount;

    /**
     * 延迟时间
     */
    private Long sleepTime;


    /**
     * 数据检查类型
     */
    private CheckType checkType;


}
