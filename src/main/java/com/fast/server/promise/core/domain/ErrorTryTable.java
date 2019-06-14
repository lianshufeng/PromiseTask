package com.fast.server.promise.core.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorTryTable extends SuperEntity {


    @DBRef(lazy = true)
    private TaskTable taskTable;


    /**
     * 尝试次数
     */
    @Indexed
    private Integer tryCount;

    /**
     * 延迟时间
     */
    @Indexed
    private Long sleepTime;


    /**
     * 尝试任务时间
     */
    @Indexed
    private Long tryTime;


}
