package com.fast.server.promise.core.constant;

import com.fast.server.promise.core.model.ErrorModel;
import com.fast.server.promise.core.model.HttpModel;
import com.fast.server.promise.core.model.RequestParmModel;
import com.fast.server.promise.core.type.MethodType;

import java.util.HashMap;

public class DefaultRequestParm {


    /**
     * 获取默认的请求模型
     *
     * @return
     */
    public static RequestParmModel get() {
        RequestParmModel parmModel = new RequestParmModel();
        // http
        HttpModel http = new HttpModel();
        http.setMethodType(MethodType.Get);
        http.setHeader(new HashMap<String, Object>() {{
            put("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        }});
        parmModel.setHttp(http);

        //error
        parmModel.setError(new ErrorModel(3, 3000l));

        //一小时
        parmModel.setExecuteTime(1000 * 60 * 60l);


        return parmModel;
    }


}
