package com.fast.server.promise.core.scheduled;

import com.fast.server.promise.core.service.TaskService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@Log
public class DoTaskScheduled {

    @Autowired
    private TaskService taskService;

    @Scheduled(cron = "0/5 * * * * ?") //每小时的33分钟开始执行，每3分钟执行1次
    public void timeOutTask() {
        this.taskService.timeOutTask();
    }

}
