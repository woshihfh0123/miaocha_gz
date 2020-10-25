package com.picture.android.Model;

/**
 * created by WWL on 2019/6/9 0009:09
 */
public class EventBusVO {
    private String code;
    private Object object;
    private Object secondObject;
    private Object thirdObject;
    private Object FourthObject;


    public EventBusVO(String code) {
        this.code = code;
    }

    public EventBusVO(String code, Object object) {
        this.code = code;
        this.object = object;
    }

    public EventBusVO(String code, Object object, Object secondObject) {
        this.code = code;
        this.object = object;
        this.secondObject = secondObject;
    }

    public EventBusVO(String code, Object object, Object secondObject, Object thirdObject) {
        this.code = code;
        this.object = object;
        this.secondObject = secondObject;
        this.thirdObject = thirdObject;
    }

    public EventBusVO(String code, Object object, Object secondObject, Object thirdObject, Object fourthObject) {
        this.code = code;
        this.object = object;
        this.secondObject = secondObject;
        this.thirdObject = thirdObject;
        FourthObject = fourthObject;
    }

    public EventBusVO() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
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
        return FourthObject;
    }

    public void setFourthObject(Object fourthObject) {
        FourthObject = fourthObject;
    }

    @Override
    public String toString() {
        return "EventBusModel{" +
                "code='" + code + '\'' +
                ", object=" + object +
                ", secondObject=" + secondObject +
                ", thirdObject=" + thirdObject +
                ", FourthObject=" + FourthObject +
                '}';
    }
}
