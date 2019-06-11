package com.fast.server.promise.core.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * 任务模型
 */

@Data
@Entity
@Table(name = "taskTable", indexes = {@Index(name = "taskId", columnList = "taskId"), @Index(name = "taskTable_createTime", columnList = "createTime"), @Index(name = "taskTable_updateTime", columnList = "updateTime")})
@NoArgsConstructor
@AllArgsConstructor
public class TaskTable extends SuperEntity {

    @Column(unique = true, nullable = true)
    private String taskId;


    /**
     * 执行时间
     */
    @Column(unique = false, nullable = true)
    private Long executeTime;


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
