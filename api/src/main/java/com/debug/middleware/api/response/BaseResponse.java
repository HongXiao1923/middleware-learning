package com.debug.middleware.api.response;

import com.debug.middleware.api.enums.StatusCode;

/**
 * 文件描述
 * 响应信息类
 * @author dingguo.an
 * @date 2022年04月22日 21:47
 */
public class BaseResponse<T> {
    //状态码
    private Integer code;
    //描述信息
    private String msg;
    //响应数据（采用泛型接受通用数据）
    private T data;

    public BaseResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResponse(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }

    public BaseResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
