package com.fast.server.promise.core.util;

import com.fast.server.promise.core.constant.DefaultRequestParm;
import com.fast.server.promise.core.model.RequestParmModel;
import org.springframework.beans.BeanUtils;

public class RequestParmUtil {


    /**
     * 构建
     *
     * @param userParm
     * @return
     */
    public static RequestParmModel build(RequestParmModel userParm) throws Exception {
        //默认参数
        RequestParmModel parm = DefaultRequestParm.get();
        BeanUtils.copyProperties(userParm, parm, BeanUtil.getNullPropertyNames(userParm));


        setRequestModel(userParm, parm, "http");
        setRequestModel(userParm, parm, "error");

        return parm;
    }


    /**
     * 设置请求模型
     *
     * @param varName
     */
    private static void setRequestModel(RequestParmModel user, RequestParmModel system, String varName) throws Exception {
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

}
