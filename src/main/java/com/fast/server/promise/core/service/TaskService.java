package com.fast.server.promise.core.service;

import com.fast.server.promise.core.model.RequestParmModel;
import com.fast.server.promise.core.model.ResponseStatusModel;
import com.fast.server.promise.core.model.TaskModel;
import com.fast.server.promise.core.util.TimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class TaskService {


    /**
     * 线程安全的队列
     */
    private Map<String, TaskModel> _tasks = new ConcurrentHashMap<>();


    /**
     * 添加任务
     *
     * @return
     */
    public ResponseStatusModel add(RequestParmModel parm) {
        String id = UUID.randomUUID().toString().replaceAll("-", "");
        TaskModel taskModel = new TaskModel();
        taskModel.setRecordTime(TimeUtil.getTime());
        BeanUtils.copyProperties(parm, taskModel);
        this._tasks.put(id, taskModel);
        return getResponseModel(id);
    }


    /**
     * 删除任务
     *
     * @param id
     * @return
     */
    public boolean remove(String id) {
        return this._tasks.remove(id) != null;
    }


    /**
     * 获取任务
     *
     * @param id
     * @return
     */

    public TaskModel query(String id) {
        return this._tasks.get(id);
    }


    /**
     * 设置心跳任务
     *
     * @param id
     */
    public ResponseStatusModel heartbeat(String id) {
        TaskModel taskModel = this._tasks.get(id);
        if (taskModel == null) {
            return null;
        }
        taskModel.setRecordTime(TimeUtil.getTime());
        return getResponseModel(id);
    }

    /**
     * 获取下次执行时间
     *
     * @param taskModel
     * @return
     */
    private long getNextTime(TaskModel taskModel) {
        return taskModel.getExecuteTime() - (TimeUtil.getTime() - taskModel.getRecordTime());
    }


    /**
     * 获取响应的模型
     *
     * @param id
     * @return
     */
    public ResponseStatusModel getResponseModel(String id) {
        TaskModel taskModel = this._tasks.get(id);
        if (taskModel == null) {
            return null;
        }
        return ResponseStatusModel.builder().id(id).nextExecuteTime(getNextTime(taskModel)).build();
    }


}
