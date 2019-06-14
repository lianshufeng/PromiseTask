package com.fast.server.promise.core.dao.extend;

import com.fast.server.promise.core.domain.TaskTable;
import com.fast.server.promise.core.type.TaskState;

import java.util.List;

public interface TaskTableDaoExtend {


    boolean removeTaskTable(String id);


    /**
     * 设置心跳时间
     *
     * @param id
     * @param time
     */
    boolean setHeartbeatTime(String id, long time);


    /**
     * 查找超时的任务
     *
     * @return
     */
    List<TaskTable> findCanDoTask();


    /**
     * 设置任务状态
     *
     * @param taskId
     * @param taskState
     * @return
     */
    boolean setTaskState(String taskId, TaskState taskState);


    /**
     * 尝试执行任务
     *
     * @param taskId
     * @return
     */
    boolean setTryDoWork(String taskId);

}
