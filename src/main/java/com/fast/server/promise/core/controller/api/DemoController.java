package com.fast.server.promise.core.controller.api;

import com.fast.server.promise.core.constant.DefaultRequestParm;
import com.fast.server.promise.core.model.RequestParmModel;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("api")
public class DemoController {


    @RequestMapping("demo")
    public RequestParmModel demo() {
        return DefaultRequestParm.get();
    }


}