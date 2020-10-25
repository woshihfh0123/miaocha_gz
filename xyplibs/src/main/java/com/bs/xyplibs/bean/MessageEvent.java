package com.bs.xyplibs.bean;

/**
 * Created by Administrator on 2018/6/8.
 * 废弃该类
 */

@Deprecated
public class MessageEvent<T> {
    public static final int EVENT_A = 1000;
    public static final int EVENT_B = 1001;
    private int code;
    private T data;

    public MessageEvent(int code) {
        this.code = code;
    }


    public MessageEvent(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "MessageEvent{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
