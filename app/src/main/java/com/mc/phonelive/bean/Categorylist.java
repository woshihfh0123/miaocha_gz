package com.mc.phonelive.bean;

import java.io.Serializable;

/**
 * 商品分类
 * created by WWL on 2020/4/18 0018:11
 */
public class Categorylist implements Serializable {
    private String id;//: "1",
    private String  cate_name;//: "好空气"

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }
}
