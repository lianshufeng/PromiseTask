package com.fast.server.promise.core.controller.api;

import com.fast.server.promise.core.constant.DefaultRequestParm;
import com.fast.server.promise.core.dao.TaskTableDao;
import com.fast.server.promise.core.domain.TaskTable;
import com.fast.server.promise.core.model.HttpModel;
import com.fast.server.promise.core.model.RequestParmModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api")
public class DemoController {

    @Autowired
    private TaskTableDao taskDao;


    @RequestMapping("demo")
    public RequestParmModel demo() {
        RequestParmModel requestParmModel = DefaultRequestParm.get();
        requestParmModel.setHttp(HttpModel.builder().url("http://www.baidu.com").build());
        return requestParmModel;
    }


    @RequestMapping("test1")
    public Object test1() {
        RequestParmModel requestParmModel = DefaultRequestParm.get();
        TaskTable taskTable = new TaskTable();
        this.taskDao.save(taskTable);
        return taskTable;
    }


    @RequestMapping("test2")
    public Object test2() {
        return this.taskDao.findAll();
    }


}
