package com.mc.phonelive.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by cxf on 2018/10/15.
 * 印象实体类
 */

public class ImpressBean {

    private String id;
    private String name;
    private String orderno;
    private String color;
    private int check;
    private String nums;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JSONField(name = "orderno")
    public String getOrder() {
        return orderno;
    }

    @JSONField(name = "orderno")
    public void setOrder(String order) {
        this.orderno = order;
    }

    @JSONField(name = "colour")
    public String getColor() {
        return color;
    }

    @JSONField(name = "colour")
    public void setColor(String color) {
        this.color = color;
    }

    @JSONField(name = "ifcheck")
    public int getCheck() {
        return check;
    }

    @JSONField(name = "ifcheck")
    public void setCheck(int check) {
        this.check = check;
    }

    public boolean isChecked() {
        return this.check == 1;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }
}
