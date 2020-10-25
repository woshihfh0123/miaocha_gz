
package com.bs.xyplibs.base;

import org.litepal.crud.LitePalSupport;

import java.io.Serializable;

public  class BaseVO<T> extends LitePalSupport implements Serializable {
    public String code;
    public String desc;
    public String time;
    public String message;
    public boolean success;
    public T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "BaseVO{" +
                "code='" + code + '\'' +
                ", desc='" + desc + '\'' +
                ", time='" + time + '\'' +
//                ", data=" + data +
                '}';
    }
}
