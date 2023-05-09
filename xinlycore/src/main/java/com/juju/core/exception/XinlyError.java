package com.juju.core.exception;

/**
 * 自定义异常
 * Created by xiangyao on 2018/10/24.
 */
public class XinlyError extends RuntimeException{
    private int code;
    private String message;

    public XinlyError(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
