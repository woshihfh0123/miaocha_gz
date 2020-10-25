package com.mc.phonelive.bean;

import java.io.Serializable;

/**
 * created by WWL on 2020/4/7 0007:18
 */
public class ShopMallGoodsList implements Serializable {
    private String id;//: "78",
    private String uid;//
    private String title;//: "营养快餐",
    private String price;//: "10.00",
    private String ot_price;//: "100.00",
    private String goods_image;//: "http://zhiboshow.yanshi.hbbeisheng.com/api/upload/store/20200305/06367191637794114.png"

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOt_price() {
        return ot_price;
    }

    public void setOt_price(String ot_price) {
        this.ot_price = ot_price;
    }

    public String getGoods_image() {
        return goods_image;
    }

    public void setGoods_image(String goods_image) {
        this.goods_image = goods_image;
    }
}
