package com.fast.server.promise.core.type;

public enum InvokerState {

    /**
     * 成功
     */
    Success("成功"),

    /**
     * 失败
     */
    Error("失败"),


    /**
     * 异常情况
     */
    Exception("异常");


    private String remark;


    InvokerState(String remark) {
        this.remark = remark;
    }

    public String getInfo() {
        return remark;
    }
}
