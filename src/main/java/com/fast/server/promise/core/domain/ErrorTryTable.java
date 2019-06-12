package com.fast.server.promise.core.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@Entity
@Table(name = "errorTryTable", indexes = {@Index(name = "errorTryTable_createTime", columnList = "createTime"), @Index(name = "errorTryTable_updateTime", columnList = "updateTime")})
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ErrorTryTable extends SuperEntity {


    @OneToOne(fetch = FetchType.LAZY)
    private TaskTable taskTable;


    /**
     * 尝试次数
     */
    @Column(unique = false, nullable = true)
    private Integer tryCount;

    /**
     * 延迟时间
     */
    @Column(unique = false, nullable = true)
    private Long sleepTime;

}
