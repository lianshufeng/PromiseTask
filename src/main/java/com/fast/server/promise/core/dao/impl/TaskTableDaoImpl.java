package com.fast.server.promise.core.dao.impl;

import com.fast.server.promise.core.dao.extend.TaskTableDaoExtend;
import com.fast.server.promise.core.domain.ErrorTryTable;
import com.fast.server.promise.core.domain.TaskTable;
import com.fast.server.promise.core.helper.DBHelper;
import com.fast.server.promise.core.type.TaskState;
import lombok.extern.java.Log;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.ArrayList;
import java.util.List;

@Log
public class TaskTableDaoImpl implements TaskTableDaoExtend {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DBHelper dbHelper;

    /**
     * 最大的数量
     */
    private final static int MaxQueryCount = 5;

    @Override
    public boolean removeTaskTable(String id) {
        TaskTable taskTable = this.mongoTemplate.findAndRemove(createQueryTaskByTaskId(id), TaskTable.class);
        if (taskTable == null) {
            return false;
        }
        //删除对应的数据
        this.mongoTemplate.remove(taskTable.getHttpTable());
        this.mongoTemplate.remove(taskTable.getErrorTryTable());
        return true;
    }

    @Override
    public boolean setHeartbeatTime(String id, long time) {
        Update update = new Update();
        update.set("heartbeatTime", this.dbHelper.getTime());
        this.dbHelper.updateTime(update);
        return this.mongoTemplate.updateFirst(createQueryTaskByTaskId(id), update, TaskTable.class).getModifiedCount() > 0;
    }

    @Override
    public List<TaskTable> findCanDoTask() {

        List<TaskTable> list = new ArrayList<>();
        for (int i = 0; i < MaxQueryCount; i++) {
            Document document = new Document();
            document.put("$where", "this.heartbeatTime + this.executeTime < " + this.dbHelper.getTime());
            document.put("taskState", "Wait");
            Query query = new BasicQuery(document);
            query.limit(1);
            Update update = new Update();
            update.set("taskState", TaskState.Work);
            this.dbHelper.updateTime(update);
            TaskTable taskTable = this.mongoTemplate.findAndModify(query, update, TaskTable.class);
            if (taskTable != null) {
                list.add(taskTable);
            } else {
                break;
            }
        }
        return list;

//        return this.mongoTemplate.findAnd(query, update, TaskTable.class);
//        return this.entityManager.createQuery("From TaskTable where  ?0 > heartbeatTime + executeTime  and taskState = 0 ", TaskTable.class).setParameter(0, TimeUtil.getTime()).setMaxResults(MaxQueryCount).getResultList();
    }

    @Override
    public boolean setTaskState(String taskId, TaskState taskState) {
        Update update = new Update();
        update.set("taskState", taskState);
        this.dbHelper.updateTime(update);
        return this.mongoTemplate.updateFirst(createQueryTaskByTaskId(taskId), update, TaskTable.class).getModifiedCount() > 0;
    }

    @Override
    public boolean setTryDoWork(String taskId) {
        TaskTable taskTable = this.mongoTemplate.findOne(createQueryTaskByTaskId(taskId), TaskTable.class);
        if (taskTable != null) {
            //设置状态
            taskTable.setTaskState(TaskState.Work);
            this.mongoTemplate.save(taskTable);

            //设置尝试时间
            ErrorTryTable errorTryTable = taskTable.getErrorTryTable();
            errorTryTable.setTryTime(this.dbHelper.getTime());
            errorTryTable.setTryCount(errorTryTable.getTryCount() - 1);
            this.mongoTemplate.save(errorTryTable);

            return true;
        }
        return false;

    }

    /**
     * 构建查询条件
     *
     * @param taskId
     * @return
     */
    private Query createQueryTaskByTaskId(String taskId) {
        return new Query().addCriteria(Criteria.where("taskId").is(taskId));
    }


//    @Autowired
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    private EntityManager entityManager;
//
//    //最大查询的任务互数量
//    private final static int MaxQueryCount = 5;
//
//    @Override
//    public boolean removeTaskTable(String id) {
//        TaskTable taskTable = findByTaskId(id);
//        if (taskTable != null) {
//            int removeHttpTable = this.entityManager.createNativeQuery("delete from http_table where id = ?0").setParameter(0, taskTable.getHttpTable().getId()).executeUpdate();
//            int removeErrorTryTable = this.entityManager.createNativeQuery("delete from error_try_table where id = ?0").setParameter(0, taskTable.getErrorTryTable().getId()).executeUpdate();
//            int removeTaskTable = this.entityManager.createNativeQuery("delete from task_table where id = ?0").setParameter(0, taskTable.getId()).executeUpdate();
//
//            log.info("remove task :" + id);
//            return removeHttpTable > 0 && removeErrorTryTable > 0 && removeTaskTable > 0;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean setHeartbeatTime(String id, long time) {
//        int size = this.entityManager.createNativeQuery("update task_table set task_id = ?0 , heartbeat_time = ?1 ").setParameter(0, id).setParameter(1, time).executeUpdate();
//        return size > 0;
//    }
//
//
//    @Override
//    public List<TaskTable> findCanDoTask() {
//        return this.entityManager.createQuery("From TaskTable where  ?0 > heartbeatTime + executeTime  and taskState = 0 ", TaskTable.class).setParameter(0, TimeUtil.getTime()).setMaxResults(MaxQueryCount).getResultList();
//    }
//
//    @Override
//    public boolean setTaskState(String taskId, TaskState taskState) {
//        try {
//            TaskTable taskTable = findByTaskId(taskId);
//            if (taskTable != null) {
//                taskTable.setTaskState(taskState);
//                this.entityManager.merge(taskTable);
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    @Override
//    public boolean setTryDoWork(String taskId) {
//        TaskTable taskTable = findByTaskId(taskId);
//        if (taskTable != null) {
//            //设置状态
//            taskTable.setTaskState(TaskState.Work);
//            this.entityManager.merge(taskTable);
//
//            //设置尝试时间
//            ErrorTryTable errorTryTable = taskTable.getErrorTryTable();
//            errorTryTable.setTryTime(TimeUtil.getTime());
//            errorTryTable.setTryCount(errorTryTable.getTryCount() - 1);
//            this.entityManager.merge(errorTryTable);
//
//        }
//        return false;
//    }
//
//
//    /**
//     * 通过任务id查询到该实体
//     *
//     * @return
//     */
//    private TaskTable findByTaskId(String taskId) {
//        try {
//            TaskTable taskTable = this.entityManager.createQuery("From TaskTable where taskId = ?0 ", TaskTable.class).setParameter(0, taskId).getSingleResult();
//            return taskTable;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
