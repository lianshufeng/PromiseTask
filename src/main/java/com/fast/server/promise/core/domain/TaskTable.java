package com.fast.server.promise.core.domain;

import com.fast.server.promise.core.type.TaskState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * 任务模型
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class TaskTable extends SuperEntity {

    @Indexed(unique = true)
    private String taskId;


    /**
     * 执行时间
     */
    @Indexed
    private Long executeTime;


    /**
     * 心跳记录时间
     */
    @Indexed
    private Long heartbeatTime;


    /**
     * http的任务
     */
    @DBRef(lazy = true)
    private HttpTable httpTable;


    /**
     * 错误模型
     */
    @DBRef(lazy = true)
    private ErrorTryTable errorTryTable;


    /**
     * 工作状态
     */
    @Indexed
    private TaskState taskState;


}
