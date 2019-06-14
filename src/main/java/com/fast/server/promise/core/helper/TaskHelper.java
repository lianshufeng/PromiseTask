package com.fast.server.promise.core.helper;

import com.fast.server.promise.core.model.TaskModel;
import com.fast.server.promise.core.service.TaskService;
import com.fast.server.promise.core.type.TaskState;
import com.fast.server.promise.core.util.HttpClientUtil;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log
@Component
public class TaskHelper {


    ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);

    @Autowired
    private TaskService taskService;

    public void doit(final String taskid) {


        this.fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                //查询到任务实体
                final TaskModel taskModel = taskService.query(taskid);
                if (taskModel == null) {
                    log.info("任务不存在 : " + taskid);
                    return;
                }
                //开始执行任务的时间\
                taskService.setTryDoWork(taskModel.getId());
                if (taskModel != null && taskModel.getHttp() != null) {
                    try {
                        log.info("执行任务: [" + taskModel.getHttp().getUrl() + "]");
                        //进行网络请求
                        HttpClientUtil.request(null, taskModel.getHttp());
                        //执行成功则删除该记录
                        taskService.remove(taskModel.getId());
                    } catch (Exception e) {
                        //设置任务状态
                        taskService.setTaskState(taskModel.getId(), TaskState.Error);

                        e.printStackTrace();
                    }

                }


            }
        });


    }


    @PreDestroy
    private void shutdown() {
        this.fixedThreadPool.shutdownNow();
    }

}
