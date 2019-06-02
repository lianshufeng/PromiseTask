package com.fast.server.promise.core.controller.api;

import com.fast.server.promise.core.constant.DefaultRequestParm;
import com.fast.server.promise.core.model.*;
import com.fast.server.promise.core.service.TaskService;
import com.fast.server.promise.core.util.BeanUtil;
import com.fast.server.promise.core.util.JsonUtil;
import lombok.extern.java.Log;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;

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
    @RequestMapping("add")
    public InvokerResult<ResponseStatusModel> add(@RequestBody @Validated RequestParmModel userModel) throws Exception {
        log("add", userModel);
        //默认参数
        RequestParmModel parm = DefaultRequestParm.get();
        BeanUtils.copyProperties(userModel, parm, BeanUtil.getNullPropertyNames(userModel));


        setRequestModel(userModel, parm, "http");
        setRequestModel(userModel, parm, "error");


        //添加任务
        return InvokerResult.notNull(this.taskService.add(parm));
    }


    /**
     * 查询任务
     *
     * @param id
     * @return
     */
    @RequestMapping("query")
    public InvokerResult<ResponseTaskModel> query(String id) {
        ResponseTaskModel responseTaskModel = new ResponseTaskModel();
        responseTaskModel.setTask(this.taskService.query(id));
        responseTaskModel.setStatus(this.taskService.getResponseModel(id));
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
     * 设置请求模型
     *
     * @param varName
     */
    private void setRequestModel(RequestParmModel user, RequestParmModel system, String varName) throws Exception {
        //取出需要拷贝的用户与系统的属性
        Object userMoudle = BeanUtil.get(user, varName);
        Object systemMoudle = BeanUtil.get(DefaultRequestParm.get(), varName);

        //复制且过滤为null的用户数据
        if (userMoudle != null) {
            BeanUtils.copyProperties(userMoudle, systemMoudle, BeanUtil.getNullPropertyNames(userMoudle));
        }


        //设置到系统数据中
        BeanUtil.set(system, varName, systemMoudle);
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
