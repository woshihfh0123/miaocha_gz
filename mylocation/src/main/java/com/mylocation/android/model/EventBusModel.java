package com.mylocation.android.model;

/**
 * @author WWL
 */

public class EventBusModel {
    private String code;
    private Object object;
    private Object secondObject;
    private Object thirdObject;
    private Object FourthObject;


    public EventBusModel(String code) {
        this.code = code;
    }

    public EventBusModel(String code, Object object) {
        this.code = code;
        this.object = object;
    }

    public EventBusModel(String code, Object object, Object secondObject) {
        this.code = code;
        this.object = object;
        this.secondObject = secondObject;
    }

    public EventBusModel(String code, Object object, Object secondObject, Object thirdObject) {
        this.code = code;
        this.object = object;
        this.secondObject = secondObject;
        this.thirdObject = thirdObject;
    }

    public EventBusModel(String code, Object object, Object secondObject, Object thirdObject, Object fourthObject) {
        this.code = code;
        this.object = object;
        this.secondObject = secondObject;
        this.thirdObject = thirdObject;
        FourthObject = fourthObject;
    }

    public EventBusModel() {
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
