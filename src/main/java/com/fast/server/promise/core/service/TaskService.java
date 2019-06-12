package com.fast.server.promise.core.service;

import com.fast.server.promise.core.dao.ErrorTryTableDao;
import com.fast.server.promise.core.dao.HttpTableDao;
import com.fast.server.promise.core.dao.TaskTableDao;
import com.fast.server.promise.core.domain.ErrorTryTable;
import com.fast.server.promise.core.domain.HttpTable;
import com.fast.server.promise.core.domain.TaskTable;
import com.fast.server.promise.core.model.*;
import com.fast.server.promise.core.type.MethodType;
import com.fast.server.promise.core.util.JsonUtil;
import com.fast.server.promise.core.util.TimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskService {


    /**
     * 线程安全的队列
     */
    private Map<String, TaskModel> _tasks = new ConcurrentHashMap<>();


    @Autowired
    private TaskTableDao taskTableDao;


    @Autowired
    private HttpTableDao httpTableDao;

    @Autowired
    private ErrorTryTableDao errorTryTableDao;


    /**
     * 添加任务
     *
     * @return
     */
//    @Transactional
    public RequestParmModel put(RequestParmModel parm) {
        if (parm.getId() == null) {
            parm.setId(UUID.randomUUID().toString().replaceAll("-", ""));
        }


        TaskModel taskModel = new TaskModel();
        taskModel.setHeartbeatTime(TimeUtil.getTime());
        BeanUtils.copyProperties(parm, taskModel);


        TaskTable taskTable = this.taskTableDao.findFirstByTaskId(taskModel.getId());
        ErrorTryTable errorTryTable = null;
        HttpTable httpTable = null;
        if (taskTable == null) {
            taskTable = new TaskTable();

            BeanUtils.copyProperties(taskModel, taskTable);
            taskTable.setTaskId(taskModel.getId());
            taskTable.setHeartbeatTime(taskModel.getHeartbeatTime());

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
    @Transactional
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
    @Transactional
    public ResponseStatusModel heartbeat(String id) {
        this.taskTableDao.setHeartbeatTime(id, TimeUtil.getTime());
        return getResponseModel(id);
    }

    /**
     * 获取下次执行时间
     *
     * @param taskModel
     * @return
     */
    private long getNextTime(TaskModel taskModel) {
        return taskModel.getExecuteTime() - (TimeUtil.getTime() - taskModel.getHeartbeatTime());
    }


    /**
     * 获取响应的模型
     *
     * @param id
     * @return
     */
    public ResponseStatusModel getResponseModel(String id) {
        TaskModel taskModel = this.query(id);
//        TaskModel taskModel = this._tasks.get(id);
        if (taskModel == null) {
            return null;
        }
        return ResponseStatusModel.builder().nextExecuteTime(getNextTime(taskModel)).build();
    }


    @Transactional
    public void timeOutTask() {

        List<TaskTable> taskTables = this.taskTableDao.findTimeOutTask();

        System.out.println("---");

    }

}
