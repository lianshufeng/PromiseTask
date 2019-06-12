package com.fast.server.promise.core.dao;

import com.fast.server.promise.core.dao.extend.TaskTableDaoExtend;
import com.fast.server.promise.core.domain.TaskTable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskTableDao extends JpaRepository<TaskTable, Long>, TaskTableDaoExtend {


    /**
     * 通过任务id查找任务
     *
     * @param taskId
     * @return
     */
    TaskTable findFirstByTaskId(String taskId);



}
