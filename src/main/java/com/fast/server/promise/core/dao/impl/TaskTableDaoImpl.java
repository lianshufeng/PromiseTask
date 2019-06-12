package com.fast.server.promise.core.dao.impl;

import com.fast.server.promise.core.dao.extend.TaskTableDaoExtend;
import com.fast.server.promise.core.domain.ErrorTryTable;
import com.fast.server.promise.core.domain.TaskTable;
import com.fast.server.promise.core.util.TimeUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.persistence.EntityManager;
import java.util.List;

@Log
public class TaskTableDaoImpl implements TaskTableDaoExtend {


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    //最大查询的任务互数量
    private final static int MaxQueryCount = 5;

    @Override
    public boolean removeTaskTable(String id) {
        TaskTable taskTable = null;
        try {
            taskTable = this.entityManager.createQuery("From TaskTable where taskId = ?0 ", TaskTable.class).setParameter(0, id).getSingleResult();
        } catch (Exception e) {
//            e.printStackTrace();
        }
        if (taskTable != null) {
            int removeHttpTable = this.entityManager.createNativeQuery("delete from http_table where id = ?0").setParameter(0, taskTable.getHttpTable().getId()).executeUpdate();
            int removeErrorTryTable = this.entityManager.createNativeQuery("delete from error_try_table where id = ?0").setParameter(0, taskTable.getErrorTryTable().getId()).executeUpdate();
            int removeTaskTable = this.entityManager.createNativeQuery("delete from task_table where id = ?0").setParameter(0, taskTable.getId()).executeUpdate();

            log.info("remove task :" + id);
            return removeHttpTable > 0 && removeErrorTryTable > 0 && removeTaskTable > 0;
        }
        return false;
    }

    @Override
    public boolean setHeartbeatTime(String id, long time) {
        int size = this.entityManager.createNativeQuery("update task_table set task_id = ?0 , heartbeat_time = ?1 ").setParameter(0, id).setParameter(1, time).executeUpdate();
        return size > 0;
    }


    @Override
    public List<TaskTable> findTimeOutTask() {
        List<TaskTable> taskTables = this.entityManager.createQuery("From TaskTable where  ?0 > heartbeatTime + executeTime ", TaskTable.class).setParameter(0, TimeUtil.getTime()).setMaxResults(MaxQueryCount).getResultList();
        for (TaskTable taskTable : taskTables) {
            ErrorTryTable errorTryTable = taskTable.getErrorTryTable();
            if (errorTryTable.getTryCount() > 0) {
                errorTryTable.setTryCount(errorTryTable.getTryCount() - 1);
                this.entityManager.merge(errorTryTable);
                taskTable.setHeartbeatTime(TimeUtil.getTime());
                this.entityManager.merge(taskTable);
            } else {
                log.info("失败次数过多，删除任务");
                this.removeTaskTable(taskTable.getTaskId());
            }

        }
        return taskTables;
    }
}
