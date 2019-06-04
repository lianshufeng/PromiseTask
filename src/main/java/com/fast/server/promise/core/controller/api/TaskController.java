package com.fast.server.promise.core.controller.api;

import com.fast.server.promise.core.model.*;
import com.fast.server.promise.core.service.TaskService;
import com.fast.server.promise.core.util.JsonUtil;
import com.fast.server.promise.core.util.RequestParmUtil;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log
@RestController
@RequestMapping("api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    /**
     * 添加任务
     *
     * @return
     */
    @RequestMapping("put")
    public InvokerResult<RequestParmModel> put(@RequestBody @Validated RequestParmModel userModel) throws Exception {
        log("put", userModel);
        //添加任务
        return InvokerResult.notNull(this.taskService.put(RequestParmUtil.build(userModel)));
    }


    /**
     * 查询任务
     *
     * @param id
     * @return
     */
    @RequestMapping("query")
    public InvokerResult<ResponseTaskModel> query(String id) {
        TaskModel taskModel = this.taskService.query(id);
        if (taskModel == null) {
            return InvokerResult.success(null);
        }
        //拷贝数据
        ResponseTaskModel responseTaskModel = new ResponseTaskModel();
        BeanUtils.copyProperties(taskModel,responseTaskModel);
        responseTaskModel.setStatus(this.taskService.getResponseModel(taskModel.getId()));
        return InvokerResult.notNull(responseTaskModel);
    }

    /**
     * 心跳
     *
     * @param id
     * @return
     */
    @RequestMapping("heartbeat")
    public InvokerResult<ResponseStatusModel> heartbeat(String id) {
        return InvokerResult.notNull(this.taskService.heartbeat(id));
    }


    /**
     * 删除任务
     *
     * @param id
     * @return
     */
    @RequestMapping("remove")
    public InvokerResult<Boolean> remove(String id) {
        return InvokerResult.notNull(this.taskService.remove(id));
    }


    /**
     * 打印日志
     *
     * @param cmd
     * @param o
     */
    private void log(String cmd, Object o) {
        try {
            log.info("[" + cmd + "] : " + JsonUtil.toJson(o));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
