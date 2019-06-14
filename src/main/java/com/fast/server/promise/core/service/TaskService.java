package com.fast.server.promise.core.service;

import com.fast.server.promise.core.dao.ErrorTryTableDao;
import com.fast.server.promise.core.dao.HttpTableDao;
import com.fast.server.promise.core.dao.TaskTableDao;
import com.fast.server.promise.core.domain.ErrorTryTable;
import com.fast.server.promise.core.domain.HttpTable;
import com.fast.server.promise.core.domain.TaskTable;
import com.fast.server.promise.core.helper.DBHelper;
import com.fast.server.promise.core.helper.TaskHelper;
import com.fast.server.promise.core.model.*;
import com.fast.server.promise.core.type.MethodType;
import com.fast.server.promise.core.type.TaskState;
import com.fast.server.promise.core.util.JsonUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class TaskService {


    @Autowired
    private TaskTableDao taskTableDao;

    @Autowired
    private HttpTableDao httpTableDao;

    @Autowired
    private ErrorTryTableDao errorTryTableDao;

    @Autowired
    private TaskHelper taskHelper;

    @Autowired
    private DBHelper dbHelper;


    /**
     * 添加任务
     *
     * @return
     */
//    
    public RequestParmModel put(RequestParmModel parm) {
        if (parm.getId() == null) {
            parm.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        }


        TaskModel taskModel = new TaskModel();
        taskModel.setHeartbeatTime(this.dbHelper.getTime());
        BeanUtils.copyProperties(parm, taskModel);


        TaskTable taskTable = this.taskTableDao.findFirstByTaskId(taskModel.getId());
        ErrorTryTable errorTryTable = null;
        HttpTable httpTable = null;
        if (taskTable == null) {
            taskTable = new TaskTable();

            BeanUtils.copyProperties(taskModel, taskTable);
            taskTable.setTaskId(taskModel.getId());
            taskTable.setHeartbeatTime(taskModel.getHeartbeatTime());
            taskTable.setTaskState(TaskState.Wait);

            this.taskTableDao.save(taskTable);

            errorTryTable = new ErrorTryTable();
            httpTable = new HttpTable();
        } else {
            errorTryTable = taskTable.getErrorTryTable();
            httpTable = taskTable.getHttpTable();
        }

        //ErrorTryTable
        BeanUtils.copyProperties(taskModel.getErrorTry(), errorTryTable);
        errorTryTable.setTaskTable(taskTable);
        this.errorTryTableDao.save(errorTryTable);

        //HttpTable
        BeanUtils.copyProperties(taskModel.getHttp(), httpTable, "header", "body");
        if (taskModel.getHttp().getBody() != null) {
            httpTable.setBody(JsonUtil.toJson(taskModel.getHttp().getBody()));
        }
        if (taskModel.getHttp().getHeader() != null) {
            httpTable.setHeader(JsonUtil.toJson(taskModel.getHttp().getHeader()));
        }
        httpTable.setTaskTable(taskTable);
        this.httpTableDao.save(httpTable);

        //TaskTable
        taskTable.setErrorTryTable(errorTryTable);
        taskTable.setHttpTable(httpTable);
        this.taskTableDao.save(taskTable);

        return taskModel;
    }


    /**
     * 删除任务
     *
     * @param id
     * @return
     */

    public boolean remove(String id) {
        return this.taskTableDao.removeTaskTable(id);
    }


    /**
     * 获取任务
     *
     * @param id
     * @return
     */

    public TaskModel query(String id) {
        try {
            TaskTable taskTable = this.taskTableDao.findFirstByTaskId(id);
            if (taskTable != null) {
                TaskModel taskModel = new TaskModel();
                BeanUtils.copyProperties(taskTable, taskModel);
                taskModel.setId(taskTable.getTaskId());

                //http
                HttpModel httpModel = new HttpModel();
                HttpTable httpTable = taskTable.getHttpTable();
                BeanUtils.copyProperties(httpTable, httpModel, "header", "body");
                if (httpTable.getHeader() != null) {
                    httpModel.setHeader(JsonUtil.toObject(httpTable.getHeader(), Map.class));
                }
                if (httpTable.getBody() != null) {
                    Class type = (httpTable.getMethod() == MethodType.Json ? Map.class : String.class);
                    httpModel.setBody(JsonUtil.toObject(httpTable.getBody(), type));
                }
                taskModel.setHttp(httpModel);


                //errorTry
                ErrorTryModel errorTryModel = new ErrorTryModel();
                ErrorTryTable errorTryTable = taskTable.getErrorTryTable();
                BeanUtils.copyProperties(errorTryTable, errorTryModel);
                taskModel.setErrorTry(errorTryModel);

                return taskModel;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }


    /**
     * 设置心跳任务
     *
     * @param id
     */

    public ResponseStatusModel heartbeat(String id) {
        this.taskTableDao.setHeartbeatTime(id, this.dbHelper.getTime());
        return getResponseModel(id);
    }


    /**
     * 设置尝试执行任务
     *
     * @param taskId
     */
    public void setTryDoWork(String taskId) {
        this.taskTableDao.setTryDoWork(taskId);
    }


    /**
     * 修改任务的状态
     *
     * @param taskId
     * @param taskState
     */
    public void setTaskState(String taskId, TaskState taskState) {
        this.taskTableDao.setTaskState(taskId, taskState);
    }

    /**
     * 获取下次执行时间
     *
     * @param taskModel
     * @return
     */
    private long getNextTime(TaskModel taskModel) {
        return taskModel.getExecuteTime() - (this.dbHelper.getTime() - taskModel.getHeartbeatTime());
    }


    /**
     * 获取响应的模型
     *
     * @param id
     * @return
     */
    public ResponseStatusModel getResponseModel(String id) {
        TaskModel taskModel = this.query(id);
        if (taskModel == null) {
            return null;
        }
        return ResponseStatusModel.builder().nextExecuteTime(getNextTime(taskModel)).build();
    }


    /**
     * 执行任务
     */

    public void doTask() {
        List<TaskTable> taskTables = this.findTimeOutTask();
        if (taskTables != null && taskTables.size() > 0) {
            for (TaskTable taskTable : taskTables) {
                this.taskHelper.doit(taskTable.getTaskId());
            }
        }
    }


    public List<TaskTable> findTimeOutTask() {
        return this.taskTableDao.findCanDoTask();
    }

}
