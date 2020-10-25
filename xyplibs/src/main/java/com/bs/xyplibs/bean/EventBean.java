package com.bs.xyplibs.bean;

/**
 * Created by Administrator on 2018/6/8.
 *
 */

public class EventBean {
    private String code;
    private Object firstObject;
    private Object secondObject;
    private Object thirdObject;
    private Object fourthObject;
    private Object fiveObject;

    public EventBean(String code) {
        this.code = code;
    }

    public EventBean(String code, Object firstObject) {
        this.code = code;
        this.firstObject = firstObject;
    }

    public EventBean(String code, Object firstObject, Object secondObject) {
        this.code = code;
        this.firstObject = firstObject;
        this.secondObject = secondObject;
    }

    public EventBean(String code, Object firstObject, Object secondObject, Object thirdObject) {
        this.code = code;
        this.firstObject = firstObject;
        this.secondObject = secondObject;
        this.thirdObject = thirdObject;
    }

    public EventBean(String code, Object firstObject, Object secondObject, Object thirdObject, Object fourthObject) {
        this.code = code;
        this.firstObject = firstObject;
        this.secondObject = secondObject;
        this.thirdObject = thirdObject;
        this.fourthObject = fourthObject;
    }
    public EventBean(String code, Object firstObject, Object secondObject, Object thirdObject, Object fourthObject,Object fiveObject) {
        this.code = code;
        this.firstObject = firstObject;
        this.secondObject = secondObject;
        this.thirdObject = thirdObject;
        this.fourthObject = fourthObject;
        this.fiveObject = fiveObject;
    }

    public Object getFiveObject() {
        return fiveObject;
    }

    public void setFiveObject(Object fiveObject) {
        this.fiveObject = fiveObject;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getFirstObject() {
        return firstObject;
    }

    public void setFirstObject(Object firstObject) {
        this.firstObject = firstObject;
    }

    public Object getSecondObject() {
        return secondObject;
    }

    public void setSecondObject(Object secondObject) {
        this.secondObject = secondObject;
    }

    public Object getThirdObject() {
        return thirdObject;
    }

    public void setThirdObject(Object thirdObject) {
        this.thirdObject = thirdObject;
    }

    public Object getFourthObject() {
        return fourthObject;
    }

    public void setFourthObject(Object fourthObject) {
        this.fourthObject = fourthObject;
    }

    @Override
    public String toString() {
        return "EventBean{" +
                "code='" + code + '\'' +
                ", firstObject=" + firstObject +
                ", secondObject=" + secondObject +
                ", thirdObject=" + thirdObject +
                ", fourthObject=" + fourthObject +
                ", fiveObject=" + fiveObject +
                '}';
    }
}
