package com.fast.server.promise.core.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseTaskModel {


    //任务模型
    private TaskModel task;

    // 响应的model
    private ResponseStatusModel status;


}
