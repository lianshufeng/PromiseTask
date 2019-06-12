package com.fast.server.promise.core.dao.extend;

import com.fast.server.promise.core.domain.TaskTable;

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
    List<TaskTable> findTimeOutTask();


}
