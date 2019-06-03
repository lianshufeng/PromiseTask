package com.fast.server.promise.core.controller.api;

import com.fast.server.promise.core.constant.DefaultRequestParm;
import com.fast.server.promise.core.model.RequestParmModel;
import com.fast.server.promise.core.util.HttpClientUtil;
import com.fast.server.promise.core.util.RequestParmUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;


@RestController
@RequestMapping("api")
public class TestController {


    @RequestMapping("test")
    public void test(HttpServletRequest request, HttpServletResponse response, @RequestBody @Validated RequestParmModel userModel) throws Exception {
        //转换为系统任务
        RequestParmModel systemParm = RequestParmUtil.build(userModel);

        //进行网络请求
        HttpClientUtil.request(response, systemParm.getHttp());

    }


}
