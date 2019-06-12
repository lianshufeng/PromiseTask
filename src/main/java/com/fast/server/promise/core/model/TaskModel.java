package com.fast.server.promise.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskModel extends RequestParmModel {

    /**
     * 系统时间
     */
    private long heartbeatTime;


}
