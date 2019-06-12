package com.fast.server.promise.core.domain;

import lombok.*;

import javax.persistence.*;

/**
 * 任务模型
 */

@Data
@Builder
@Entity
@Table(name = "taskTable", indexes = {@Index(name = "taskId", columnList = "taskId"), @Index(name = "taskTable_createTime", columnList = "createTime"), @Index(name = "taskTable_updateTime", columnList = "updateTime"), @Index(name = "taskTable_executeTime", columnList = "executeTime"), @Index(name = "taskTable_heartbeatTime", columnList = "heartbeatTime")})
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaskTable extends SuperEntity {

    @Column(unique = true, nullable = true)
    private String taskId;


    /**
     * 执行时间
     */
    @Column(unique = false, nullable = true)
    private Long executeTime;


    /**
     * 心跳记录时间
     */
    @Column(unique = false, nullable = true)
    private Long heartbeatTime;


    /**
     * http的任务
     */
    @OneToOne
    private HttpTable httpTable;


    /**
     * 错误模型
     */
    @OneToOne
    private ErrorTryTable errorTryTable;


}
