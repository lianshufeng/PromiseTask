package com.fast.server.promise.core.util;

import com.fast.server.promise.core.model.HttpModel;
import com.fast.server.promise.core.type.MethodType;
import org.apache.http.Header;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class HttpClientUtil {


    /**
     * 网络请求
     */
    public static void request(HttpServletResponse httpServletResponse, HttpModel httpModel) throws IOException {


        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpRequestBase requestBase = null;
        //请求类型的判断
        if (httpModel.getMethod() == MethodType.Get) {
            requestBase = new HttpGet(httpModel.getUrl());
        } else if (httpModel.getMethod() == MethodType.Post || httpModel.getMethod() == MethodType.Json) {
            HttpPost httpPost = new HttpPost(httpModel.getUrl());
            httpPost.setEntity(buildHttpEntity(httpModel));
            requestBase = httpPost;
        }

        //设置请求头
        for (Map.Entry<String, Object> entry : httpModel.getHeader().entrySet()) {
            requestBase.setHeader(entry.getKey(), String.valueOf(entry.getValue()));
        }

        //开始请求
        CloseableHttpResponse response = httpclient.execute(requestBase);
        //转发请求头
        for (Header header : response.getAllHeaders()) {
            httpServletResponse.setHeader(header.getName(), header.getValue());
        }
        //转发数据流
        OutputStream outputStream = null;
        InputStream inputStream = null;

        try {
            outputStream = httpServletResponse.getOutputStream();
            inputStream = response.getEntity().getContent();
            StreamUtils.copy(inputStream, outputStream);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        }


    }


    /**
     * 创建数据
     *
     * @param httpModel
     * @return
     */
    private static StringEntity buildHttpEntity(HttpModel httpModel) {
        String body = null;
        String mimeType = null;
        if (httpModel.getMethod() == MethodType.Post) {
            mimeType = "application/x-www-form-urlencoded";
            body = httpModel.getBody() == null ? "" : String.valueOf(httpModel.getBody());
        } else if (httpModel.getMethod() == MethodType.Json) {
            mimeType = "application/json";
            body = httpModel.getBody() == null ? "{}" : JsonUtil.toJson(httpModel.getBody());
        }
        return new StringEntity(body, ContentType.create(mimeType, httpModel.getCharset()));
    }


}
