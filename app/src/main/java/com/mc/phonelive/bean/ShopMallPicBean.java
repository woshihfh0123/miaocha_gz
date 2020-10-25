package com.mc.phonelive.bean;

import java.io.Serializable;

/**
 * created by WWL on 2020/4/7 0007:17
 */
public class ShopMallPicBean implements Serializable {
    private String slide_pic;//: "",
    private String slide_url;//: "",
    private String info_id;//: "89"
    private String goods_store_uid;//: "89"

    public String getGoods_store_uid() {
        return goods_store_uid;
    }

    public void setGoods_store_uid(String goods_store_uid) {
        this.goods_store_uid = goods_store_uid;
    }

    public String getSlide_pic() {
        return slide_pic;
    }

    public void setSlide_pic(String slide_pic) {
        this.slide_pic = slide_pic;
    }

    public String getSlide_url() {
        return slide_url;
    }

    public void setSlide_url(String slide_url) {
        this.slide_url = slide_url;
    }

    public String getInfo_id() {
        return info_id;
    }

    public void setInfo_id(String info_id) {
        this.info_id = info_id;
    }
}
